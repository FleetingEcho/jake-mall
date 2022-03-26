
package com.jake.mall.api.admin.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CarouselEditParam {

    @ApiModelProperty("Carousel id")
    @NotNull(message = "Carousel id cannot be empty")
    @Min(1)
    private Integer carouselId;

    @ApiModelProperty("Carousel URL address")
    @NotEmpty(message = "Carousel URL cannot be empty")
    private String carouselUrl;

    @ApiModelProperty("Carousel Jumping url")
    @NotEmpty(message = "Carousel Jumping url cannot be empty")
    private String redirectUrl;

    @ApiModelProperty("sort number")
    @Min(value = 1, message = "carouselRank min to 1")
    @Max(value = 200, message = "carouselRank max to 200")
    @NotNull(message = "carouselRank cannot be empty")
    private Integer carouselRank;
}
