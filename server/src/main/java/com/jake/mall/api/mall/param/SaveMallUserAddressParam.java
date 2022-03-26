
package com.jake.mall.api.mall.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * add receiving address param
 */
@Data
public class SaveMallUserAddressParam {

    @ApiModelProperty("Name of recipient")
    private String userName;

    @ApiModelProperty("recipient phone")
    private String userPhone;

    @ApiModelProperty("default address 0-No 1-Yes")
    private Byte defaultFlag;

    @ApiModelProperty("Province")
    private String provinceName;

    @ApiModelProperty("City")
    private String cityName;

    @ApiModelProperty("Region")
    private String regionName;

    @ApiModelProperty("Detail address")
    private String detailAddress;
}
