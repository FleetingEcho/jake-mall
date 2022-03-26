
package com.jake.mall.api.mall.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * save orders' param
 */
@Data
public class SaveOrderParam implements Serializable {

    @ApiModelProperty("Array of order item ids")
    private Long[] cartItemIds;

    @ApiModelProperty("address id")
    private Long addressId;
}
