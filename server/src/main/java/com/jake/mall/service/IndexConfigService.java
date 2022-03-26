
package com.jake.mall.service;

import com.jake.mall.api.mall.vo.IndexConfigGoodsVO;
import com.jake.mall.entity.IndexConfig;
import com.jake.mall.util.PageQueryUtil;
import com.jake.mall.util.PageResult;

import java.util.List;

public interface IndexConfigService {

    /**
     * Return a fixed number of home page configuration commodity objects (home page call)
     *
     * @param number
     * @return
     */
    List<IndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number);

    /**
     * pagination
     *
     * @param pageUtil
     * @return
     */
    PageResult getConfigsPage(PageQueryUtil pageUtil);

    String saveIndexConfig(IndexConfig indexConfig);

    String updateIndexConfig(IndexConfig indexConfig);

    IndexConfig getIndexConfigById(Long id);

    Boolean deleteBatch(Long[] ids);
}
