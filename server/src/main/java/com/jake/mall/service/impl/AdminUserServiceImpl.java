
package com.jake.mall.service.impl;

import com.jake.mall.common.ServiceResultEnum;
import com.jake.mall.dao.AdminUserMapper;
import com.jake.mall.dao.AdminUserTokenMapper;
import com.jake.mall.entity.AdminUser;
import com.jake.mall.entity.AdminUserToken;
import com.jake.mall.service.AdminUserService;
import com.jake.mall.util.NumberUtil;
import com.jake.mall.util.SystemUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Resource
    private AdminUserMapper adminUserMapper;

    @Resource
    private AdminUserTokenMapper adminUserTokenMapper;

    @Override
    public String login(String userName, String password) {
        System.out.println(userName+":::"+password);
        AdminUser loginAdminUser = adminUserMapper.login(userName, password);
        System.out.println(loginAdminUser);
        if (loginAdminUser != null) {
            //Implement the action to modify Token after logging in
            String token = getNewToken(System.currentTimeMillis() + "", loginAdminUser.getAdminUserId());
            AdminUserToken adminUserToken = adminUserTokenMapper.selectByPrimaryKey(loginAdminUser.getAdminUserId());
            //current time
            Date now = new Date();
            //expire time
            Date expireTime = new Date(now.getTime() + 2 * 24 * 3600 * 1000);//过期时间 48 小时
            if (adminUserToken == null) {
                adminUserToken = new AdminUserToken();
                adminUserToken.setAdminUserId(loginAdminUser.getAdminUserId());
                adminUserToken.setToken(token);
                adminUserToken.setUpdateTime(now);
                adminUserToken.setExpireTime(expireTime);
                //add a new token data
                if (adminUserTokenMapper.insertSelective(adminUserToken) > 0) {
                    //return generated data
                    return token;
                }
            } else {
                adminUserToken.setToken(token);
                adminUserToken.setUpdateTime(now);
                adminUserToken.setExpireTime(expireTime);
                //update user token
                if (adminUserTokenMapper.updateByPrimaryKeySelective(adminUserToken) > 0) {
                    //return token after updated.
                    return token;
                }
            }

        }
        return ServiceResultEnum.LOGIN_ERROR.getResult();
    }


    /**
     * get token
     *
     * @param timeStr
     * @param userId
     * @return
     */
    private String getNewToken(String timeStr, Long userId) {
        String src = timeStr + userId + NumberUtil.genRandomNum(6);
        return SystemUtil.genToken(src);
    }


    @Override
    public AdminUser getUserDetailById(Long loginUserId) {
        return adminUserMapper.selectByPrimaryKey(loginUserId);
    }

    @Override
    public Boolean updatePassword(Long loginUserId, String originalPassword, String newPassword) {
        AdminUser adminUser = adminUserMapper.selectByPrimaryKey(loginUserId);
        //Current users can make changes
        if (adminUser != null) {
            //Compare whether the original password is correct
            if (originalPassword.equals(adminUser.getLoginPassword())) {
                //Set new password and modify
                adminUser.setLoginPassword(newPassword);
                //Return to True in successful modification and clear the current token
                return adminUserMapper.updateByPrimaryKeySelective(adminUser) > 0 && adminUserTokenMapper.deleteByPrimaryKey(loginUserId) > 0;
            }
        }
        return false;
    }

    @Override
    public Boolean updateName(Long loginUserId, String loginUserName, String nickName) {
        AdminUser adminUser = adminUserMapper.selectByPrimaryKey(loginUserId);
        //The current user can only be changed if it is not empty
        if (adminUser != null) {
            //Set new name and modify
            adminUser.setLoginUserName(loginUserName);
            adminUser.setNickName(nickName);
            //Returns true after success
            return adminUserMapper.updateByPrimaryKeySelective(adminUser) > 0;
        }
        return false;
    }

    @Override
    public Boolean logout(Long adminUserId) {
        return adminUserTokenMapper.deleteByPrimaryKey(adminUserId) > 0;
    }
}
