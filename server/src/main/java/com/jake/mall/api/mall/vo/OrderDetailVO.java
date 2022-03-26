
package com.jake.mall.api.mall.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Order Details VO
 */
@Data
public class OrderDetailVO implements Serializable {

    @ApiModelProperty("OrderId")
    private String orderNo;

    @ApiModelProperty("Order Price")
    private Integer totalPrice;

    @ApiModelProperty("Order Payment StatusCode")
    private Byte payStatus;

    @ApiModelProperty("Order Payment Method")
    private Byte payType;

    @ApiModelProperty("Order Payment Method")
    private String payTypeString;

    @ApiModelProperty("Order Payment Time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT-4")
    private Date payTime;

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
