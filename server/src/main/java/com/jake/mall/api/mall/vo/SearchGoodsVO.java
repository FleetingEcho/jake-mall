
package com.jake.mall.api.mall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *  Search the listings page Shopping ItemVO
 */
@Data
public class SearchGoodsVO implements Serializable {

    @ApiModelProperty("Shopping Item id")
    private Long goodsId;

    @ApiModelProperty("Shopping Item Name")
    private String goodsName;

    @ApiModelProperty("Shopping Item Introduction")
    private String goodsIntro;

    @ApiModelProperty("Shopping Item image url")
    private String goodsCoverImg;

    @ApiModelProperty("Shopping Item price")
    private Integer sellingPrice;

}
