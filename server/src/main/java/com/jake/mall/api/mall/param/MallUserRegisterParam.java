
package com.jake.mall.api.mall.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * User register param
 */
@Data
public class MallUserRegisterParam implements Serializable {

    @ApiModelProperty("Login username")
    @NotEmpty(message = "Username cannot be empty.")
    private String loginName;

    @ApiModelProperty("Password")
    @NotEmpty(message = "Password cannot be empty.")
    private String password;
}
