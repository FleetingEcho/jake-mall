
package com.jake.mall.api.mall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Receiving address VO
 */
@Data
public class UserAddressVO {

    @ApiModelProperty("Address id")
    private Long addressId;

    @ApiModelProperty("user id")
    private Long userId;

    @ApiModelProperty("Recipient Name")
    private String userName;

    @ApiModelProperty("Recipient phone")
    private String userPhone;

    @ApiModelProperty("Address default  0-No 1-Yes")
    private Byte defaultFlag;

    @ApiModelProperty("Province")
    private String provinceName;

    @ApiModelProperty("City")
    private String cityName;

    @ApiModelProperty("Region")
    private String regionName;

    @ApiModelProperty("Full Address")
    private String detailAddress;
}
