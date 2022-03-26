
package com.jake.mall.api.mall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class IndexInfoVO implements Serializable {

    @ApiModelProperty("Carousel List")
    private List<IndexCarouselVO> carousels;

    @ApiModelProperty("Popular Shopping Item(List)")
    private List<IndexConfigGoodsVO> hotGoodses;

    @ApiModelProperty("HomeNew Products(List)")
    private List<IndexConfigGoodsVO> newGoodses;

    @ApiModelProperty("Home Recommended Shopping Items(List)")
    private List<IndexConfigGoodsVO> recommendGoodses;
}
