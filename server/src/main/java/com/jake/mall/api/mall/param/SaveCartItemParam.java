
package com.jake.mall.api.mall.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * add cart item param
 */
@Data
public class SaveCartItemParam implements Serializable {

    @ApiModelProperty("goods count")
    private Integer goodsCount;

    @ApiModelProperty("Good id")
    private Long goodsId;
}
