
package com.jake.mall.dao;

import com.jake.mall.entity.MallGoodsEntity;
import com.jake.mall.util.PageQueryUtil;
import com.jake.mall.entity.StockNumDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface GoodsMapper {
    int deleteByPrimaryKey(Long goodsId);

    int insert(MallGoodsEntity record);

    int insertSelective(MallGoodsEntity record);

    MallGoodsEntity selectByPrimaryKey(Long goodsId);

    MallGoodsEntity selectByCategoryIdAndName(@Param("goodsName") String goodsName, @Param("goodsCategoryId") Long goodsCategoryId);

    int updateByPrimaryKeySelective(MallGoodsEntity record);

    int updateByPrimaryKeyWithBLOBs(MallGoodsEntity record);

    int updateByPrimaryKey(MallGoodsEntity record);

    List<MallGoodsEntity> findMallGoodsList(PageQueryUtil pageUtil);

    int getTotalMallGoods(PageQueryUtil pageUtil);

    List<MallGoodsEntity> selectByPrimaryKeys(List<Long> goodsIds);

    List<MallGoodsEntity> findMallGoodsListBySearch(PageQueryUtil pageUtil);

    int getTotalMallGoodsBySearch(PageQueryUtil pageUtil);

    int batchInsert(@Param("mallGoodsList") List<MallGoodsEntity> mallGoodsList);

    int updateStockNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);

    int batchUpdateSellStatus(@Param("orderIds")Long[] orderIds,@Param("sellStatus") int sellStatus);

}
