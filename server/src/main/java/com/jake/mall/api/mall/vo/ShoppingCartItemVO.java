
package com.jake.mall.api.mall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Shopping cart page shopping items VO
 */
@Data
public class ShoppingCartItemVO implements Serializable {

    @ApiModelProperty("Shopping cart item id")
    private Long cartItemId;

    @ApiModelProperty("Shopping Item id")
    private Long goodsId;

    @ApiModelProperty("Shopping Items count")
    private Integer goodsCount;

    @ApiModelProperty("Shopping Item Name")
    private String goodsName;

    @ApiModelProperty("Shopping Item image url")
    private String goodsCoverImg;

    @ApiModelProperty("Shopping Item price")
    private Integer sellingPrice;
}
