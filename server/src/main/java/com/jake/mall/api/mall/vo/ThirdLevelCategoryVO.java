
package com.jake.mall.api.mall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Home Category Data VO (Level 3)
 */
@Data
public class ThirdLevelCategoryVO implements Serializable {

    @ApiModelProperty("Current tertiary category id")
    private Long categoryId;

    @ApiModelProperty("Current classification level")
    private Byte categoryLevel;

    @ApiModelProperty("Current three-tier classification Name")
    private String categoryName;
}
