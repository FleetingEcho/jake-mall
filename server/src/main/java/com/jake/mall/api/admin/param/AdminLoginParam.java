
package com.jake.mall.api.admin.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class AdminLoginParam implements Serializable {

    @ApiModelProperty("Login name ")
    @NotEmpty(message = "Login name  cannot be empty")
    private String userName;

    @ApiModelProperty("User password (requires MD5 encryption)")
    @NotEmpty(message = "Password cannot be empty")
    private String passwordMd5;
}
