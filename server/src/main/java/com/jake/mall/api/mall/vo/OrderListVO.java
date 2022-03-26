
package com.jake.mall.api.mall.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Order List Page VO
 */
@Data
public class OrderListVO implements Serializable {

    private Long orderId;

    @ApiModelProperty("OrderId")
    private String orderNo;

    @ApiModelProperty("Order Price")
    private Integer totalPrice;

    @ApiModelProperty("Order Payment Method")
    private Byte payType;

    @ApiModelProperty("Order StatusCode")
    private Byte orderStatus;

    @ApiModelProperty("Order Status")
    private String orderStatusString;

    @ApiModelProperty("Order created time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT-4")
    private Date createTime;

    @ApiModelProperty("Orders List")
    private List<OrderItemVO> orderItemVOS;
}
