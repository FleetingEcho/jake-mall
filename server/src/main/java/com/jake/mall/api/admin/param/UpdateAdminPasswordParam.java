
package com.jake.mall.api.admin.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UpdateAdminPasswordParam {

    @NotEmpty(message = "originalPassword cannot be empty")
    private String originalPassword;

    @NotEmpty(message = "newPassword cannot be empty")
    private String newPassword;
}
