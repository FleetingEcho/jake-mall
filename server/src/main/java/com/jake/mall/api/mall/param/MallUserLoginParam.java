
package com.jake.mall.api.mall.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * User Login param
 */
@Data
public class MallUserLoginParam implements Serializable {

    @ApiModelProperty("Login username")
    @NotEmpty(message = "Username cannot be empty.")
    private String loginName;

    @ApiModelProperty("User password (requires MD5 encryption)")
    @NotEmpty(message = "Password cannot be empty")
    private String passwordMd5;
}
