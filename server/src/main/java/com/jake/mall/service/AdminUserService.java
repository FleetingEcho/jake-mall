
package com.jake.mall.service;

import com.jake.mall.entity.AdminUser;

public interface AdminUserService {

    String login(String userName, String password);

    AdminUser getUserDetailById(Long loginUserId);

    Boolean updatePassword(Long loginUserId, String originalPassword, String newPassword);

    Boolean updateName(Long loginUserId, String loginUserName, String nickName);

    Boolean logout(Long adminUserId);


}
