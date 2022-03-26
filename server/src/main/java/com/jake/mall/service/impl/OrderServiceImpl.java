
package com.jake.mall.service.impl;

import com.jake.mall.api.mall.vo.OrderDetailVO;
import com.jake.mall.api.mall.vo.OrderItemVO;
import com.jake.mall.api.mall.vo.OrderListVO;
import com.jake.mall.api.mall.vo.ShoppingCartItemVO;
import com.jake.mall.common.*;
import com.jake.mall.dao.*;
import com.jake.mall.entity.*;
import com.jake.mall.service.OrderService;
import com.jake.mall.util.BeanUtil;
import com.jake.mall.util.NumberUtil;
import com.jake.mall.util.PageQueryUtil;
import com.jake.mall.util.PageResult;
import com.jake.mall.common.*;
import com.jake.mall.dao.*;
import com.jake.mall.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private ShoppingCartItemMapper shoppingCartItemMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private OrderAddressMapper orderAddressMapper;

    @Override
    public OrderDetailVO getOrderDetailByOrderId(Long orderId) {
        OrderEntity orderEntity = orderMapper.selectByPrimaryKey(orderId);
        if (orderEntity == null) {
            MyException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        List<OrderItemEntity> orderItems = orderItemMapper.selectByOrderId(orderEntity.getOrderId());
        //get order details
        if (!CollectionUtils.isEmpty(orderItems)) {
            List<OrderItemVO> orderItemVOS = BeanUtil.copyList(orderItems, OrderItemVO.class);
            OrderDetailVO orderDetailVO = new OrderDetailVO();
            BeanUtil.copyProperties(orderEntity, orderDetailVO);
            orderDetailVO.setOrderStatusString(OrderStatusEnum.getOrderStatusEnumByStatus(orderDetailVO.getOrderStatus()).getName());
            orderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(orderDetailVO.getPayType()).getName());
            orderDetailVO.setOrderItemVOS(orderItemVOS);
            return orderDetailVO;
        } else {
            MyException.fail(ServiceResultEnum.ORDER_ITEM_NULL_ERROR.getResult());
            return null;
        }
    }

    @Override
    public OrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId) {
        OrderEntity orderEntity = orderMapper.selectByOrderNo(orderNo);
        if (orderEntity == null) {
            MyException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        if (!userId.equals(orderEntity.getUserId())) {
            MyException.fail(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        List<OrderItemEntity> orderItems = orderItemMapper.selectByOrderId(orderEntity.getOrderId());
        //get order details
        if (CollectionUtils.isEmpty(orderItems)) {
            MyException.fail(ServiceResultEnum.ORDER_ITEM_NOT_EXIST_ERROR.getResult());
        }
        List<OrderItemVO> orderItemVOS = BeanUtil.copyList(orderItems, OrderItemVO.class);
        OrderDetailVO orderDetailVO = new OrderDetailVO();
        BeanUtil.copyProperties(orderEntity, orderDetailVO);
        orderDetailVO.setOrderStatusString(OrderStatusEnum.getOrderStatusEnumByStatus(orderDetailVO.getOrderStatus()).getName());
        orderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(orderDetailVO.getPayType()).getName());
        orderDetailVO.setOrderItemVOS(orderItemVOS);
        return orderDetailVO;
    }


    @Override
    public PageResult getMyOrders(PageQueryUtil pageUtil) {
        int total = orderMapper.getTotalOrders(pageUtil);
        List<OrderEntity> orderEntities = orderMapper.findOrderList(pageUtil);
        List<OrderListVO> orderListVOS = new ArrayList<>();
        if (total > 0) {
            //Data conversion converts an entity class to VO
            orderListVOS = BeanUtil.copyList(orderEntities, OrderListVO.class);
            //Set the order status Chinese display value
            for (OrderListVO orderListVO : orderListVOS) {
                orderListVO.setOrderStatusString(OrderStatusEnum.getOrderStatusEnumByStatus(orderListVO.getOrderStatus()).getName());
            }
            List<Long> orderIds = orderEntities.stream().map(OrderEntity::getOrderId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderIds)) {
                List<OrderItemEntity> orderItems = orderItemMapper.selectByOrderIds(orderIds);
                Map<Long, List<OrderItemEntity>> itemByOrderIdMap = orderItems.stream().collect(groupingBy(OrderItemEntity::getOrderId));
                for (OrderListVO orderListVO : orderListVOS) {
                    //Package the order item data for each order list object
                    if (itemByOrderIdMap.containsKey(orderListVO.getOrderId())) {
                        List<OrderItemEntity> orderItemListTemp = itemByOrderIdMap.get(orderListVO.getOrderId());
                        //Convert ORDERITEM object list to ORDERITEMVO object list
                        List<OrderItemVO> orderItemVOS = BeanUtil.copyList(orderItemListTemp, OrderItemVO.class);
                        orderListVO.setOrderItemVOS(orderItemVOS);
                    }
                }
            }
        }
        PageResult pageResult = new PageResult(orderListVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String cancelOrder(String orderNo, Long userId) {
        OrderEntity orderEntity = orderMapper.selectByOrderNo(orderNo);
        if (orderEntity != null) {
            //Verify that whether it is the order under UserID, otherwise an error is reported
            if (!userId.equals(orderEntity.getUserId())) {
                MyException.fail(ServiceResultEnum.NO_PERMISSION_ERROR.getResult());
            }
            //check order status
            if (orderEntity.getOrderStatus().intValue() == OrderStatusEnum.ORDER_SUCCESS.getOrderStatus()
                    || orderEntity.getOrderStatus().intValue() == OrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()
                    || orderEntity.getOrderStatus().intValue() == OrderStatusEnum.ORDER_CLOSED_BY_EXPIRED.getOrderStatus()
                    || orderEntity.getOrderStatus().intValue() == OrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) {
                return ServiceResultEnum.ORDER_STATUS_ERROR.getResult();
            }
            if (orderMapper.closeOrder(Collections.singletonList(orderEntity.getOrderId()), OrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String finishOrder(String orderNo, Long userId) {
        OrderEntity orderEntity = orderMapper.selectByOrderNo(orderNo);
        if (orderEntity != null) {
            //Verify that whether it is the order under UserID, otherwise an error is reported
            if (!userId.equals(orderEntity.getUserId())) {
                return ServiceResultEnum.NO_PERMISSION_ERROR.getResult();
            }
            //The order status is determined that the modification is not performed without the storage state.
            if (orderEntity.getOrderStatus().intValue() != OrderStatusEnum.ORDER_EXPRESS.getOrderStatus()) {
                return ServiceResultEnum.ORDER_STATUS_ERROR.getResult();
            }
            orderEntity.setOrderStatus((byte) OrderStatusEnum.ORDER_SUCCESS.getOrderStatus());
            orderEntity.setUpdateTime(new Date());
            if (orderMapper.updateByPrimaryKeySelective(orderEntity) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String paySuccess(String orderNo, int payType) {
        OrderEntity orderEntity = orderMapper.selectByOrderNo(orderNo);
        if (orderEntity != null) {
            //No modification is not performed without payment
            if (orderEntity.getOrderStatus().intValue() != OrderStatusEnum.ORDER_PRE_PAY.getOrderStatus()) {
                return ServiceResultEnum.ORDER_STATUS_ERROR.getResult();
            }
            orderEntity.setOrderStatus((byte) OrderStatusEnum.ORDER_PAID.getOrderStatus());
            orderEntity.setPayType((byte) payType);
            orderEntity.setPayStatus((byte) PayStatusEnum.PAY_SUCCESS.getPayStatus());
            orderEntity.setPayTime(new Date());
            orderEntity.setUpdateTime(new Date());
            if (orderMapper.updateByPrimaryKeySelective(orderEntity) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    @Transactional
    public String saveOrder(MallUser loginMallUser, MallUserAddress address, List<ShoppingCartItemVO> myShoppingCartItems) {
        List<Long> itemIdList = myShoppingCartItems.stream().map(ShoppingCartItemVO::getCartItemId).collect(Collectors.toList());
        List<Long> goodsIds = myShoppingCartItems.stream().map(ShoppingCartItemVO::getGoodsId).collect(Collectors.toList());
        List<MallGoodsEntity> mallGoods = goodsMapper.selectByPrimaryKeys(goodsIds);
        //Check if you have a product
        List<MallGoodsEntity> goodsListNotSelling = mallGoods.stream()
                .filter(mallGoodsTemp -> mallGoodsTemp.getGoodsSellStatus() != Constants.SELL_STATUS_UP)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(goodsListNotSelling)) {
            //goodsListNotSelling  || Objects are not empty, indicating that there is a product
            MyException.fail(goodsListNotSelling.get(0).getGoodsName() + "Down for sale, unable to generate orders");
        }
        Map<Long, MallGoodsEntity> mallGoodsMap = mallGoods.stream().collect(Collectors.toMap(MallGoodsEntity::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
        //Judgment product inventory
        for (ShoppingCartItemVO shoppingCartItemVO : myShoppingCartItems) {
            //Isolated from the product data in the shopping cart in the goods, directly return to the wrong reminder
            if (!mallGoodsMap.containsKey(shoppingCartItemVO.getGoodsId())) {
                MyException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
            }
            //There is a case where the number is greater than inventory, return exception
            if (shoppingCartItemVO.getGoodsCount() > mallGoodsMap.get(shoppingCartItemVO.getGoodsId()).getStockNum()) {
                MyException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
            }
        }
        //Delete shopping items
        if (!CollectionUtils.isEmpty(itemIdList) && !CollectionUtils.isEmpty(goodsIds) && !CollectionUtils.isEmpty(mallGoods)) {
            if (shoppingCartItemMapper.deleteBatch(itemIdList) > 0) {
                List<StockNumDTO> stockNumDTOS = BeanUtil.copyList(myShoppingCartItems, StockNumDTO.class);
                int updateStockNumResult = goodsMapper.updateStockNum(stockNumDTOS);
                if (updateStockNumResult < 1) {
                    MyException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
                }
                //generate order id
                String orderNo = NumberUtil.genOrderNo();
                int priceTotal = 0;
                //save order
                OrderEntity orderEntity = new OrderEntity();
                orderEntity.setOrderNo(orderNo);
                orderEntity.setUserId(loginMallUser.getUserId());
                //count all prices
                for (ShoppingCartItemVO shoppingCartItemVO : myShoppingCartItems) {
                    priceTotal += shoppingCartItemVO.getGoodsCount() * shoppingCartItemVO.getSellingPrice();
                }
                if (priceTotal < 1) {
                    MyException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                orderEntity.setTotalPrice(priceTotal);
                String extraInfo = "";
                orderEntity.setExtraInfo(extraInfo);
                //Generate an order item and save the order item record
                if (orderMapper.insertSelective(orderEntity) > 0) {
                    //Generate an order receipt address and save to the database
                    OrderAddressEntity orderAddressEntity = new OrderAddressEntity();
                    BeanUtil.copyProperties(address, orderAddressEntity);
                    orderAddressEntity.setOrderId(orderEntity.getOrderId());
                    //Generate all order items and save them to the database
                    List<OrderItemEntity> orderItemEntities = new ArrayList<>();
                    for (ShoppingCartItemVO shoppingCartItemVO : myShoppingCartItems) {
                        OrderItemEntity orderItemEntity = new OrderItemEntity();
                        //map ShoppingCartItemVO's properties to OrderItem
                        BeanUtil.copyProperties(shoppingCartItemVO, orderItemEntity);
                        //useGeneratedKeys get order ID
                        orderItemEntity.setOrderId(orderEntity.getOrderId());
                        orderItemEntities.add(orderItemEntity);
                    }
                    //save to database
                    if (orderItemMapper.insertBatch(orderItemEntities) > 0 && orderAddressMapper.insertSelective(orderAddressEntity) > 0) {
                        //After all operations are successful, return the order number for the controller method to jump to order details
                        return orderNo;
                    }
                    MyException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                MyException.fail(ServiceResultEnum.DB_ERROR.getResult());
            }
            MyException.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        MyException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        return ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult();
    }


    @Override
    public PageResult getOrdersPage(PageQueryUtil pageUtil) {
        List<OrderEntity> orderEntities = orderMapper.findOrderList(pageUtil);
        int total = orderMapper.getTotalOrders(pageUtil);
        PageResult pageResult = new PageResult(orderEntities, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    @Transactional
    public String updateOrderInfo(OrderEntity orderEntity) {
        OrderEntity temp = orderMapper.selectByPrimaryKey(orderEntity.getOrderId());
        //update details condition: not null and orderStatus>=0  and status <3
        if (temp != null && temp.getOrderStatus() >= 0 && temp.getOrderStatus() < 3) {
            temp.setTotalPrice(orderEntity.getTotalPrice());
            temp.setUpdateTime(new Date());
            if (orderMapper.updateByPrimaryKeySelective(temp) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            }
            return ServiceResultEnum.DB_ERROR.getResult();
        }
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkDone(Long[] ids) {
        //Query all orders judgments status modified status and update time
        List<OrderEntity> orders = orderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        StringBuilder errorOrderNos = new StringBuilder();
        if (!CollectionUtils.isEmpty(orders)) {
            for (OrderEntity orderEntity : orders) {
                if (orderEntity.getIsDeleted() == 1) {
                    errorOrderNos.append(orderEntity.getOrderNo()).append(" ");
                    continue;
                }
                if (orderEntity.getOrderStatus() != 1) {
                    errorOrderNos.append(orderEntity.getOrderNo()).append(" ");
                }
            }
            if (StringUtils.isEmpty(errorOrderNos.toString())) {
                //Order status can be performed to perform the delivery completion operation to modify the order status and update time
                if (orderMapper.checkDone(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //Order is not executable at this time
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "The status of the order is not the success of the payment.";
                } else {
                    return "You have selected too many orders with a status other than Paid successfully for the dispatch to be completed";
                }
            }
        }
        //Unqualified data returns an error prompt
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkOut(Long[] ids) {
        //Query all orders judgments status modified status and update time
        List<OrderEntity> orders = orderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        StringBuilder errorOrderNos = new StringBuilder();
        if (!CollectionUtils.isEmpty(orders)) {
            for (OrderEntity orderEntity : orders) {
                if (orderEntity.getIsDeleted() == 1) {
                    errorOrderNos.append(orderEntity.getOrderNo()).append(" ");
                    continue;
                }
                if (orderEntity.getOrderStatus() != 1 && orderEntity.getOrderStatus() != 2) {
                    errorOrderNos.append(orderEntity.getOrderNo()).append(" ");
                }
            }
            if (StringUtils.isEmpty(errorOrderNos.toString())) {
                //Order status can perform the output operation to modify the order status and update time
                if (orderMapper.checkOut(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //Order is not executable at this time
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "The status of an order that is not paid for or dispatched cannot be dispatched.";
                } else {
                    return "You have selected too many orders with a status other than paid or dispatched to perform an outbound operation";
                }
            }
        }
        //Unqualified data returns an error prompt
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String closeOrder(Long[] ids) {
        //Query all orders Determine status Modify status and update time
        List<OrderEntity> orders = orderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        StringBuilder errorOrderNos = new StringBuilder();
        if (!CollectionUtils.isEmpty(orders)) {
            for (OrderEntity orderEntity : orders) {
                // isDeleted=1  means closed order
                if (orderEntity.getIsDeleted() == 1) {
                    errorOrderNos.append(orderEntity.getOrderNo()).append(" ");
                    continue;
                }
                //Closed or completed orders cannot be closed
                if (orderEntity.getOrderStatus() == 4 || orderEntity.getOrderStatus() < 0) {
                    errorOrderNos.append(orderEntity.getOrderNo()).append(" ");
                }
            }
            if (StringUtils.isEmpty(errorOrderNos.toString())) {
                //Order status is normal Close operation can be performed Modify order status and update time
                if (orderMapper.closeOrder(Arrays.asList(ids), OrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //Orders cannot be closed at this time
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "Orders cannot be closed";
                } else {
                    return "The order you have selected cannot be closed";
                }
            }
        }
        //No data found ,Return to error message
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    public List<OrderItemVO> getOrderItems(Long orderId) {
        OrderEntity orderEntity = orderMapper.selectByPrimaryKey(orderId);
        if (orderEntity != null) {
            List<OrderItemEntity> orderItems = orderItemMapper.selectByOrderId(orderEntity.getOrderId());
            //Get order item data
            if (!CollectionUtils.isEmpty(orderItems)) {
                List<OrderItemVO> orderItemVOs = BeanUtil.copyList(orderItems, OrderItemVO.class);
                return orderItemVOs;
            }
        }
        return null;
    }
}
