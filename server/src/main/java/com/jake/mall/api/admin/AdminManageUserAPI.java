
package com.jake.mall.api.admin;

import com.jake.mall.common.ServiceResultEnum;
import com.jake.mall.config.annotation.TokenToAdminUser;
import com.jake.mall.entity.AdminUser;
import com.jake.mall.service.AdminUserService;
import io.swagger.annotations.Api;
import com.jake.mall.api.admin.param.AdminLoginParam;
import com.jake.mall.api.admin.param.UpdateAdminNameParam;
import com.jake.mall.api.admin.param.UpdateAdminPasswordParam;
import com.jake.mall.common.Constants;
import com.jake.mall.entity.AdminUserToken;
import com.jake.mall.util.Result;
import com.jake.mall.util.AppRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;


@RestController
@Api(value = "v1", tags = "Admin Manage User API")
@RequestMapping("/manage-api/v1")
public class AdminManageUserAPI {

    @Resource
    private AdminUserService adminUserService;

    private static final Logger logger = LoggerFactory.getLogger(AdminManageUserAPI.class);

    @RequestMapping(value = "/adminUser/login", method = RequestMethod.POST)
    public Result<Object> login(@RequestBody @Valid AdminLoginParam adminLoginParam) {
        String loginResult = adminUserService.login(adminLoginParam.getUserName(), adminLoginParam.getPasswordMd5());
        logger.info("manage login api,adminName={},loginResult={}", adminLoginParam.getUserName(), loginResult);

        //success
        if (!StringUtils.isEmpty(loginResult) && loginResult.length() == Constants.TOKEN_LENGTH) {
            Result<Object> result = AppRes.ok();
            result.setData(loginResult);
            return result;
        }
        //fail
        return AppRes.error(loginResult);
    }

    @RequestMapping(value = "/adminUser/profile", method = RequestMethod.GET)
    public Result<Object> profile(@TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        AdminUser adminUserEntity = adminUserService.getUserDetailById(adminUser.getAdminUserId());
        if (adminUserEntity != null) {
            adminUserEntity.setLoginPassword("******");
            Result<Object> result = AppRes.ok();
            result.setData(adminUserEntity);
            return result;
        }
        return AppRes.error(ServiceResultEnum.DATA_NOT_EXIST.getResult());
    }

    @RequestMapping(value = "/adminUser/password", method = RequestMethod.PUT)
    public Result<Object> passwordUpdate(@RequestBody @Valid UpdateAdminPasswordParam adminPasswordParam, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        if (adminUserService.updatePassword(adminUser.getAdminUserId(), adminPasswordParam.getOriginalPassword(), adminPasswordParam.getNewPassword())) {
            return AppRes.ok();
        } else {
            return AppRes.error(ServiceResultEnum.DB_ERROR.getResult());
        }
    }

    @RequestMapping(value = "/adminUser/name", method = RequestMethod.PUT)
    public Result<Object> nameUpdate(@RequestBody @Valid UpdateAdminNameParam adminNameParam, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        if (adminUserService.updateName(adminUser.getAdminUserId(), adminNameParam.getLoginUserName(), adminNameParam.getNickName())) {
            return AppRes.ok();
        } else {
            return AppRes.error(ServiceResultEnum.DB_ERROR.getResult());
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.DELETE)
    public Result<Object> logout(@TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        adminUserService.logout(adminUser.getAdminUserId());
        return AppRes.ok();
    }

}
