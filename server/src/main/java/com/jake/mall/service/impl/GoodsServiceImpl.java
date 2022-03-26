
package com.jake.mall.service.impl;

import com.jake.mall.api.mall.vo.SearchGoodsVO;
import com.jake.mall.common.CategoryLevelEnum;
import com.jake.mall.common.MyException;
import com.jake.mall.common.ServiceResultEnum;
import com.jake.mall.dao.GoodsCategoryMapper;
import com.jake.mall.dao.GoodsMapper;
import com.jake.mall.entity.GoodsCategory;
import com.jake.mall.entity.MallGoodsEntity;
import com.jake.mall.service.GoodsService;
import com.jake.mall.util.BeanUtil;
import com.jake.mall.util.PageQueryUtil;
import com.jake.mall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;

    @Override
    public PageResult getMallGoodsEntityPage(PageQueryUtil pageUtil) {
        List<MallGoodsEntity> goodsList = goodsMapper.findMallGoodsList(pageUtil);
        int total = goodsMapper.getTotalMallGoods(pageUtil);
        PageResult pageResult = new PageResult(goodsList, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveMallGoodsEntity(MallGoodsEntity goods) {
        GoodsCategory goodsCategory = goodsCategoryMapper.selectByPrimaryKey(goods.getGoodsCategoryId());
        // The classification does not exist or is not a three-level classification, the parameter field is abnormal.
        if (goodsCategory == null || goodsCategory.getCategoryLevel().intValue() != CategoryLevelEnum.LEVEL_THREE.getLevel()) {
            return ServiceResultEnum.GOODS_CATEGORY_ERROR.getResult();
        }
        if (goodsMapper.selectByCategoryIdAndName(goods.getGoodsName(), goods.getGoodsCategoryId()) != null) {
            return ServiceResultEnum.SAME_GOODS_EXIST.getResult();
        }
        if (goodsMapper.insertSelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public void batchSaveMallGoodsEntity(List<MallGoodsEntity> mallGoodsList) {
        if (!CollectionUtils.isEmpty(mallGoodsList)) {
            goodsMapper.batchInsert(mallGoodsList);
        }
    }

    @Override
    public String updateMallGoodsEntity(MallGoodsEntity goods) {
        GoodsCategory goodsCategory = goodsCategoryMapper.selectByPrimaryKey(goods.getGoodsCategoryId());
        // The classification does not exist or is not a three-level classification, the parameter field is abnormal.
        if (goodsCategory == null || goodsCategory.getCategoryLevel().intValue() != CategoryLevelEnum.LEVEL_THREE.getLevel()) {
            return ServiceResultEnum.GOODS_CATEGORY_ERROR.getResult();
        }
        MallGoodsEntity temp = goodsMapper.selectByPrimaryKey(goods.getGoodsId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        MallGoodsEntity temp2 = goodsMapper.selectByCategoryIdAndName(goods.getGoodsName(), goods.getGoodsCategoryId());
        if (temp2 != null && !temp2.getGoodsId().equals(goods.getGoodsId())) {
            //Name and classification IDs and different IDs cannot continue to modify
            return ServiceResultEnum.SAME_GOODS_EXIST.getResult();
        }
        goods.setUpdateTime(new Date());
        if (goodsMapper.updateByPrimaryKeySelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public MallGoodsEntity getMallGoodsEntityById(Long id) {
        MallGoodsEntity mallGoods = goodsMapper.selectByPrimaryKey(id);
        if (mallGoods == null) {
            MyException.fail(ServiceResultEnum.GOODS_NOT_EXIST.getResult());
        }
        return mallGoods;
    }

    @Override
    public Boolean batchUpdateSellStatus(Long[] ids, int sellStatus) {
        return goodsMapper.batchUpdateSellStatus(ids, sellStatus) > 0;
    }

    @Override
    public PageResult searchMallGoodsEntity(PageQueryUtil pageUtil) {
        List<MallGoodsEntity> goodsList = goodsMapper.findMallGoodsListBySearch(pageUtil);
        int total = goodsMapper.getTotalMallGoodsBySearch(pageUtil);
        List<SearchGoodsVO> searchGoodsVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsList)) {
            searchGoodsVOS = BeanUtil.copyList(goodsList, SearchGoodsVO.class);
            for (SearchGoodsVO searchGoodsVO : searchGoodsVOS) {
                String goodsName = searchGoodsVO.getGoodsName();
                String goodsIntro = searchGoodsVO.getGoodsIntro();
                // String too long leads to problems
                if (goodsName.length() > 28) {
                    goodsName = goodsName.substring(0, 28) + "...";
                    searchGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 30) {
                    goodsIntro = goodsIntro.substring(0, 30) + "...";
                    searchGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        return new PageResult(searchGoodsVOS, total, pageUtil.getLimit(), pageUtil.getPage());
    }
}
