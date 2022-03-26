
package com.jake.mall.service;

import com.jake.mall.entity.MallGoodsEntity;
import com.jake.mall.util.PageQueryUtil;
import com.jake.mall.util.PageResult;

import java.util.List;

public interface GoodsService {
    /**
     * paging
     *
     * @param pageUtil
     * @return
     */
    PageResult getMallGoodsEntityPage(PageQueryUtil pageUtil);

    /**
     * Adding goods
     *
     * @param goods
     * @return
     */
    String saveMallGoodsEntity(MallGoodsEntity goods);

    /**
     * New product data in batches
     *
     * @param mallGoodsList
     * @return
     */
    void batchSaveMallGoodsEntity(List<MallGoodsEntity> mallGoodsList);

    /**
     *
     *Modify product information
     * @param goods
     * @return
     */
    String updateMallGoodsEntity(MallGoodsEntity goods);

    /**
     * Batch modification sales status (on the shelf)
     *
     * @param ids
     * @return
     */
    Boolean batchUpdateSellStatus(Long[] ids, int sellStatus);

    /**
     * fetch goods' info
     *
     * @param id
     * @return
     */
    MallGoodsEntity getMallGoodsEntityById(Long id);

    /**
     * Search goods
     *
     * @param pageUtil
     * @return
     */
    PageResult searchMallGoodsEntity(PageQueryUtil pageUtil);
}
