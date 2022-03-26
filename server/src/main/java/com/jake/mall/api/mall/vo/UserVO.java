
package com.jake.mall.api.mall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserVO implements Serializable {

    @ApiModelProperty("Nick Name")
    private String nickName;

    @ApiModelProperty("Login Name")
    private String loginName;

    @ApiModelProperty("Introudction sign")
    private String introduceSign;
}
