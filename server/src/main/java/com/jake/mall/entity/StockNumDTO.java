
package com.jake.mall.entity;

import lombok.Data;

/**
 * Inventory modification
 */

@Data
public class StockNumDTO {
    private Long goodsId;

    private Integer goodsCount;
}
