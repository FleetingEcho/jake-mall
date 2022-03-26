
package com.jake.mall.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ShoppingCartItemEntity {
    private Long cartItemId;

    private Long userId;

    private Long goodsId;

    private Integer goodsCount;

    private Byte isDeleted;

    private Date createTime;

    private Date updateTime;
}
