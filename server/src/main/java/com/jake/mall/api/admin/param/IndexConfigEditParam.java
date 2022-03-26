
package com.jake.mall.api.admin.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class IndexConfigEditParam {

    @ApiModelProperty(" Config id")
    @NotNull(message = "configId cannot be empty")
    @Min(value = 1, message = "configId cannot be empty")
    private Long configId;

    @ApiModelProperty(" Config 的 Name")
    @NotEmpty(message = "configName cannot be empty")
    private String configName;

    @ApiModelProperty(" Config category")
    @NotNull(message = "configType cannot be empty")
    @Min(value = 1, message = "configType min to 1")
    @Max(value = 5, message = "configType max to 5")
    private Byte configType;

    @ApiModelProperty("Shopping Item id")
    @NotNull(message = "Shopping Item id cannot be empty")
    @Min(value = 1, message = "Shopping Item id cannot be empty")
    private Long goodsId;

    @ApiModelProperty("sort weight")
    @Min(value = 1, message = "configRank min to 1")
    @Max(value = 200, message = "configRank max to 200")
    @NotNull(message = "configRank cannot be empty")
    private Integer configRank;
}
