
package com.jake.mall.dao;

import com.jake.mall.entity.OrderAddressEntity;
import org.springframework.stereotype.Component;

@Component
public interface OrderAddressMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(OrderAddressEntity record);

    int insertSelective(OrderAddressEntity record);

    OrderAddressEntity selectByPrimaryKey(Long orderId);

    int updateByPrimaryKeySelective(OrderAddressEntity record);

    int updateByPrimaryKey(OrderAddressEntity record);
}
