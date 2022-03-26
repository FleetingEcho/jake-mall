
package com.jake.mall.api.mall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Home Category Data VO (Level 2)
 */
@Data
public class SecondLevelCategoryVO implements Serializable {

    @ApiModelProperty("Current secondary category id")
    private Long categoryId;

    @ApiModelProperty("Parent category id")
    private Long parentId;

    @ApiModelProperty("Current classification level")
    private Byte categoryLevel;

    @ApiModelProperty("Current classification level Name")
    private String categoryName;

    @ApiModelProperty("List of three categories")
    private List<ThirdLevelCategoryVO> thirdLevelCategoryVOS;
}
