
package com.jake.mall.api.mall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *  HomePage Category Data VO
 */
@Data
public class IndexCategoryVO implements Serializable {

    @ApiModelProperty("First category id")
    private Long categoryId;

    @ApiModelProperty("Current classification level")
    private Byte categoryLevel;

    @ApiModelProperty("Current Level 1 Category Name")
    private String categoryName;

    @ApiModelProperty("List of secondary categories")
    private List<SecondLevelCategoryVO> secondLevelCategoryVOS;
}
