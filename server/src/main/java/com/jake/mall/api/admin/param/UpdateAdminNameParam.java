
package com.jake.mall.api.admin.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UpdateAdminNameParam {

    @NotEmpty(message = "loginUserName cannot be empty")
    private String loginUserName;

    @NotEmpty(message = "nickName cannot be empty")
    private String nickName;
}
