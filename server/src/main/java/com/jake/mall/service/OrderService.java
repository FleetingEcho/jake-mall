
package com.jake.mall.service;

import com.jake.mall.api.mall.vo.OrderDetailVO;
import com.jake.mall.api.mall.vo.OrderItemVO;
import com.jake.mall.api.mall.vo.ShoppingCartItemVO;
import com.jake.mall.entity.MallUser;
import com.jake.mall.entity.MallUserAddress;
import com.jake.mall.entity.OrderEntity;
import com.jake.mall.util.PageQueryUtil;
import com.jake.mall.util.PageResult;

import java.util.List;

public interface OrderService {
    /**
     * fetch order info
     *
     * @param orderId
     * @return
     */
    OrderDetailVO getOrderDetailByOrderId(Long orderId);

    /**
     * fetch order details
     *
     * @param orderNo
     * @param userId
     * @return
     */
    OrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId);

    /**
     * order list
     *
     * @param pageUtil
     * @return
     */
    PageResult getMyOrders(PageQueryUtil pageUtil);

    /**
     * cancel order
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String cancelOrder(String orderNo, Long userId);

    /**
     * confirm order
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String finishOrder(String orderNo, Long userId);

    String paySuccess(String orderNo, int payType);

    String saveOrder(MallUser loginMallUser, MallUserAddress address, List<ShoppingCartItemVO> itemsForSave);

    /**
     * Pagination
     *
     * @param pageUtil
     * @return
     */
    PageResult getOrdersPage(PageQueryUtil pageUtil);

    /**
     * Order information modification
     *
     * @param orderEntity
     * @return
     */
    String updateOrderInfo(OrderEntity orderEntity);

    /**
     * Distribute
     *
     * @param ids
     * @return
     */
    String checkDone(Long[] ids);

    /**
     * Ex warehouse
     *
     * @param ids
     * @return
     */
    String checkOut(Long[] ids);

    /**
     * close order
     *
     * @param ids
     * @return
     */
    String closeOrder(Long[] ids);

    List<OrderItemVO> getOrderItems(Long orderId);
}
