
package com.jake.mall.api.mall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Order item VO on the order details page
 */
@Data
public class OrderItemVO implements Serializable {

    @ApiModelProperty("Shopping Item id")
    private Long goodsId;

    @ApiModelProperty("Shopping Items count")
    private Integer goodsCount;

    @ApiModelProperty("Shopping Item name")
    private String goodsName;

    @ApiModelProperty("Shopping Item image")
    private String goodsCoverImg;

    @ApiModelProperty("Shopping Item price")
    private Integer sellingPrice;
}
