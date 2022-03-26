
package com.jake.mall.api.mall.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * update shopping carts param
 */
@Data
public class UpdateCartItemParam implements Serializable {

    @ApiModelProperty("Shopping item id")
    private Long cartItemId;

    @ApiModelProperty("Items count")
    private Integer goodsCount;
}
