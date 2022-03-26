
package com.jake.mall.api.mall.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * User update info param
 */
@Data
public class MallUserUpdateParam implements Serializable {

    @ApiModelProperty("NickName")
    private String nickName;

    @ApiModelProperty("User password (requires MD5 encryption)")
    private String passwordMd5;

    @ApiModelProperty("Introduction sign")
    private String introduceSign;

}
