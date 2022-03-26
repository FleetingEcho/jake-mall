
package com.jake.mall.dao;

import com.jake.mall.entity.OrderItemEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface OrderItemMapper {
    int deleteByPrimaryKey(Long orderItemId);

    int insert(OrderItemEntity record);

    int insertSelective(OrderItemEntity record);

    OrderItemEntity selectByPrimaryKey(Long orderItemId);

    /**
     * Get the list item list according to the order ID
     *
     * @param orderId
     * @return
     */
    List<OrderItemEntity> selectByOrderId(Long orderId);

    /**
     *Get the list item list according to the order IDS
     *
     * @param orderIds
     * @return
     */
    List<OrderItemEntity> selectByOrderIds(@Param("orderIds") List<Long> orderIds);

    /**
     * Batch INSERT order item data
     *
     * @param orderItems
     * @return
     */
    int insertBatch(@Param("orderItems") List<OrderItemEntity> orderItems);

    int updateByPrimaryKeySelective(OrderItemEntity record);

    int updateByPrimaryKey(OrderItemEntity record);
}
