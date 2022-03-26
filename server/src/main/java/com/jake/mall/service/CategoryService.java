
package com.jake.mall.service;

import com.jake.mall.api.mall.vo.IndexCategoryVO;
import com.jake.mall.entity.GoodsCategory;
import com.jake.mall.util.PageQueryUtil;
import com.jake.mall.util.PageResult;

import java.util.List;

public interface CategoryService {

    String saveCategory(GoodsCategory goodsCategory);

    String updateGoodsCategory(GoodsCategory goodsCategory);

    GoodsCategory getGoodsCategoryById(Long id);

    Boolean deleteBatch(Long[] ids);

    /**
     * Returns classified data (home page call)
     *
     * @return
     */
    List<IndexCategoryVO> getCategoriesForIndex();

    /**
     *
     *pagination
     * @param pageUtil
     * @return
     */
    PageResult getCategorisPage(PageQueryUtil pageUtil);

    /**
     * Get classification list according to ParentID and Level
     *
     * @param parentIds
     * @param categoryLevel
     * @return
     */
    List<GoodsCategory> selectByLevelAndParentIdsAndNumber(List<Long> parentIds, int categoryLevel);
}
