
package com.jake.mall.service;

import com.jake.mall.api.mall.param.MallUserUpdateParam;
import com.jake.mall.util.PageQueryUtil;
import com.jake.mall.util.PageResult;

public interface UserService {

    /**
     * register
     *
     * @param loginName
     * @param password
     * @return
     */
    String register(String loginName, String password);


    /**
     * login
     *
     * @param loginName
     * @param passwordMD5
     * @return
     */
    String login(String loginName, String passwordMD5);

    /**
     * user info updates
     *
     * @param mallUser
     * @return
     */
    Boolean updateUserInfo(MallUserUpdateParam mallUser, Long userId);

    /**
     * log out
     * @param userId
     * @return
     */
    Boolean logout(Long userId);

    /**
     *
     *User disabled and release disabled (0- unlocked 1- is locked)
     * @param ids
     * @param lockStatus
     * @return
     */
    Boolean lockUsers(Long[] ids, int lockStatus);

    /**
     * Pagination
     *
     * @param pageUtil
     * @return
     */
    PageResult getUsersPage(PageQueryUtil pageUtil);
}
