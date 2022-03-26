
package com.jake.mall.common;

public enum OrderStatusEnum {
    /*-9*/
    DEFAULT(-9, "ERROR"),
    ORDER_PRE_PAY(0, "Waiting for payment"),
    ORDER_PAID(1, "Paid"),
    ORDER_PACKAGED(2, "Allocation complete"),
    ORDER_EXPRESS(3, "Successful exit from storage"),
    ORDER_SUCCESS(4, "Successful transaction"),
    ORDER_CLOSED_BY_MALLUSER(-1, "Manual closure"),
    ORDER_CLOSED_BY_EXPIRED(-2, "Timeout closure"),
    ORDER_CLOSED_BY_JUDGE(-3, "Merchant closure");

    private int orderStatus;

    private String name;

    OrderStatusEnum(int orderStatus, String name) {
        this.orderStatus = orderStatus;
        this.name = name;
    }

    public static OrderStatusEnum getOrderStatusEnumByStatus(int orderStatus) {
        for (OrderStatusEnum orderStatusEnum : OrderStatusEnum.values()) {
            if (orderStatusEnum.getOrderStatus() == orderStatus) {
                return orderStatusEnum;
            }
        }
        return DEFAULT;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
