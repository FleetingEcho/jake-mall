
package com.jake.mall.api.admin.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class BatchIdParam implements Serializable {
    //id list
    Long[] ids;
}
