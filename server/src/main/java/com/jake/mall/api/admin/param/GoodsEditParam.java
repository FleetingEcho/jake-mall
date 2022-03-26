
package com.jake.mall.api.admin.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class GoodsEditParam {

    @ApiModelProperty("Shopping Item id")
    @NotNull(message = "Shopping Item id cannot be empty")
    @Min(value = 1, message = "Shopping Item id cannot be empty")
    private Long goodsId;

    @ApiModelProperty("Shopping Item Name")
    @NotEmpty(message = "Shopping Item Name cannot be empty")
    @Length(max = 128,message = "Shopping Item Name content too long")
    private String goodsName;

    @ApiModelProperty("Shopping Item description ")
    @NotEmpty(message = "Shopping Item description  cannot be empty")
    @Length(max = 200,message = "Shopping Item description  content too long")
    private String goodsIntro;

    @ApiModelProperty("Category id")
    @NotNull(message = "Category id cannot be empty")
    @Min(value = 1, message = "Category id min to 1")
    private Long goodsCategoryId;

    @ApiModelProperty("Shopping Item image ")
    @NotEmpty(message = "Shopping Item image  cannot be empty")
    private String goodsCoverImg;

    @ApiModelProperty("originalPrice")
    @NotNull(message = "originalPrice cannot be empty")
    @Min(value = 1, message = "originalPrice min to 1")
    @Max(value = 1000000, message = "originalPrice max to 1000000")
    private Integer originalPrice;

    @ApiModelProperty("sellingPrice")
    @NotNull(message = "sellingPrice cannot be empty")
    @Min(value = 1, message = "sellingPrice min to 1")
    @Max(value = 1000000, message = "sellingPrice max to 1000000")
    private Integer sellingPrice;

    @ApiModelProperty("Stock")
    @NotNull(message = "Stock cannot be empty")
    @Min(value = 1, message = "Stock min to 1")
    @Max(value = 100000, message = "Stock max to 100000")
    private Integer stockNum;

    @ApiModelProperty("Shopping Item tag ")
    @NotEmpty(message = "Shopping Item tag  cannot be empty")
    @Length(max = 16,message = "Shopping Item tag  content too long")
    private String tag;

    private Byte goodsSellStatus;

    @ApiModelProperty("Shopping Item detail ")
    @NotEmpty(message = "Shopping Item detail  cannot be empty")
    private String goodsDetailContent;
}
