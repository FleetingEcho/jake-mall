
package com.jake.mall.api.mall.vo;

import com.jake.mall.entity.GoodsCategory;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Search Page Category Data VO
 */
@Data
public class SearchPageCategoryVO implements Serializable {

    private String firstLevelCategoryName;

    private List<GoodsCategory> secondLevelCategoryList;

    private String secondLevelCategoryName;

    private List<GoodsCategory> thirdLevelCategoryList;

    private String currentCategoryName;
}
