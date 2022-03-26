
package com.jake.mall.api.mall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Homepage Config Shopping ItemVO
 */
@Data
public class IndexConfigGoodsVO implements Serializable {

    @ApiModelProperty("Shopping Item id")
    private Long goodsId;
    @ApiModelProperty("Shopping Item name")
    private String goodsName;
    @ApiModelProperty("Shopping Item intro")
    private String goodsIntro;
    @ApiModelProperty("Shopping Item image address")
    private String goodsCoverImg;
    @ApiModelProperty("Shopping Item price")
    private Integer sellingPrice;
    @ApiModelProperty("Shopping Item tag")
    private String tag;
}
