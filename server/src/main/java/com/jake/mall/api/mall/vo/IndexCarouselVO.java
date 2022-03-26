
package com.jake.mall.api.mall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Carousel VO
 */
@Data
public class IndexCarouselVO implements Serializable {

    @ApiModelProperty("Carousel image address")
    private String carouselUrl;

    @ApiModelProperty("Carousel jumping url")
    private String redirectUrl;
}
