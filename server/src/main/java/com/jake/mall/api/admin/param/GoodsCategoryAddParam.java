
package com.jake.mall.api.admin.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class GoodsCategoryAddParam {

    @ApiModelProperty("Category level")
    @NotNull(message = "categoryLevel cannot be empty")
    @Min(value = 1, message = "Category level min to 1")
    @Max(value = 3, message = "Category level max to 3")
    private Byte categoryLevel;

    @ApiModelProperty("Parent category id")
    @NotNull(message = "parentId cannot be empty")
    @Min(value = 0, message = "parentId min to 0")
    private Long parentId;

    @ApiModelProperty("Category  Name")
    @NotEmpty(message = "categoryName cannot be empty")
    @Length(max = 16,message = "Category  Name过长")
    private String categoryName;

    @ApiModelProperty("sort weight")
    @Min(value = 1, message = "categoryRank min to 1")
    @Max(value = 200, message = "categoryRank max to 200")
    @NotNull(message = "categoryRank cannot be empty")
    private Integer categoryRank;
}
