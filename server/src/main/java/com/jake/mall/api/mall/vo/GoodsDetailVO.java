
package com.jake.mall.api.mall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Shopping Item VO
 */
@Data
public class GoodsDetailVO implements Serializable {

    @ApiModelProperty("Shopping Item id")
    private Long goodsId;

    @ApiModelProperty("Shopping Item name")
    private String goodsName;

    @ApiModelProperty("Shopping Item introduction")
    private String goodsIntro;

    @ApiModelProperty("Shopping Item image address")
    private String goodsCoverImg;

    @ApiModelProperty("Shopping Item price")
    private Integer sellingPrice;

    @ApiModelProperty("Shopping Item tag")
    private String tag;

    @ApiModelProperty("Shopping Item image")
    private String[] goodsCarouselList;

    @ApiModelProperty("Shopping Item price")
    private Integer originalPrice;

    @ApiModelProperty("Shopping Item detail")
    private String goodsDetailContent;
}
