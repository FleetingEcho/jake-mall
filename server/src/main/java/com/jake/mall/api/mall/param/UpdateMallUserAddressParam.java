
package com.jake.mall.api.mall.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * update receiving address param
 */
@Data
public class UpdateMallUserAddressParam {

    @ApiModelProperty("Address id")
    private Long addressId;

    @ApiModelProperty("User id")
    private Long userId;

    @ApiModelProperty("Recipient username")
    private String userName;

    @ApiModelProperty("Recipient Contact")
    private String userPhone;

    @ApiModelProperty("Default address flag 0-No 1-Yes")
    private Byte defaultFlag;

    @ApiModelProperty("Province")
    private String provinceName;

    @ApiModelProperty("City")
    private String cityName;

    @ApiModelProperty("Region")
    private String regionName;

    @ApiModelProperty("Address")
    private String detailAddress;
}
