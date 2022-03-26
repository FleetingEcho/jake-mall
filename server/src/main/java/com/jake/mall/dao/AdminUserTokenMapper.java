
package com.jake.mall.dao;

import com.jake.mall.entity.AdminUserToken;
import org.springframework.stereotype.Component;

@Component
public interface AdminUserTokenMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(AdminUserToken record);

    int insertSelective(AdminUserToken record);

    AdminUserToken selectByPrimaryKey(Long userId);

    AdminUserToken selectByToken(String token);

    int updateByPrimaryKeySelective(AdminUserToken record);

    int updateByPrimaryKey(AdminUserToken record);
}
