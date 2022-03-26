
package com.jake.mall.service.impl;

import com.jake.mall.api.mall.param.SaveCartItemParam;
import com.jake.mall.api.mall.param.UpdateCartItemParam;
import com.jake.mall.api.mall.vo.ShoppingCartItemVO;
import com.jake.mall.common.Constants;
import com.jake.mall.common.MyException;
import com.jake.mall.common.ServiceResultEnum;
import com.jake.mall.dao.GoodsMapper;
import com.jake.mall.dao.ShoppingCartItemMapper;
import com.jake.mall.entity.MallGoodsEntity;
import com.jake.mall.entity.ShoppingCartItemEntity;
import com.jake.mall.service.ShoppingCartService;
import com.jake.mall.util.BeanUtil;
import com.jake.mall.util.PageQueryUtil;
import com.jake.mall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartItemMapper shoppingCartItemMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public String saveCartItem(SaveCartItemParam saveCartItemParam, Long userId) {
        ShoppingCartItemEntity temp = shoppingCartItemMapper.selectByUserIdAndGoodsId(userId, saveCartItemParam.getGoodsId());
        if (temp != null) {
            //Modify the record if it already exists
            MyException.fail(ServiceResultEnum.SHOPPING_CART_ITEM_EXIST_ERROR.getResult());
        }
        MallGoodsEntity mallGoods = goodsMapper.selectByPrimaryKey(saveCartItemParam.getGoodsId());
        //empty goods
        if (mallGoods == null) {
            return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
        }
        int totalItem = shoppingCartItemMapper.selectCountByUserId(userId);
        //Exceeding the minimum number of individual items
        if (saveCartItemParam.getGoodsCount() < 1) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_NUMBER_ERROR.getResult();
        }
        //Exceeding the maximum number of individual items
        if (saveCartItemParam.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //more than max number
        if (totalItem > Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_TOTAL_NUMBER_ERROR.getResult();
        }
        ShoppingCartItemEntity shoppingCartItemEntity = new ShoppingCartItemEntity();
        BeanUtil.copyProperties(saveCartItemParam, shoppingCartItemEntity);
        shoppingCartItemEntity.setUserId(userId);
        //Record keeping
        if (shoppingCartItemMapper.insertSelective(shoppingCartItemEntity) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateCartItem(UpdateCartItemParam updateCartItemParam, Long userId) {
        ShoppingCartItemEntity shoppingCartItemEntityUpdate = shoppingCartItemMapper.selectByPrimaryKey(updateCartItemParam.getCartItemId());
        if (shoppingCartItemEntityUpdate == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        if (!shoppingCartItemEntityUpdate.getUserId().equals(userId)) {
            MyException.fail(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        //Exceeding the maximum number of individual items
        if (updateCartItemParam.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //The userId of the current login account is different from the userId of the cartItem to be modified, error returned
        if (!shoppingCartItemEntityUpdate.getUserId().equals(userId)) {
            return ServiceResultEnum.NO_PERMISSION_ERROR.getResult();
        }
        //If the values are the same, no data operation is performed
        if (updateCartItemParam.getGoodsCount().equals(shoppingCartItemEntityUpdate.getGoodsCount())) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        shoppingCartItemEntityUpdate.setGoodsCount(updateCartItemParam.getGoodsCount());
        shoppingCartItemEntityUpdate.setUpdateTime(new Date());
        //modify records
        if (shoppingCartItemMapper.updateByPrimaryKeySelective(shoppingCartItemEntityUpdate) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public ShoppingCartItemEntity getCartItemById(Long ShoppingCartItemId) {
        ShoppingCartItemEntity shoppingCartItemEntity = shoppingCartItemMapper.selectByPrimaryKey(ShoppingCartItemId);
        if (shoppingCartItemEntity == null) {
            MyException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return shoppingCartItemEntity;
    }

    @Override
    public Boolean deleteById(Long shoppingCartItemId, Long userId) {
        ShoppingCartItemEntity shoppingCartItemEntity = shoppingCartItemMapper.selectByPrimaryKey(shoppingCartItemId);
        if (shoppingCartItemEntity == null) {
            return false;
        }
        //userId not the same , cannot delete.
        if (!userId.equals(shoppingCartItemEntity.getUserId())) {
            return false;
        }
        return shoppingCartItemMapper.deleteByPrimaryKey(shoppingCartItemId) > 0;
    }

    @Override
    public List<ShoppingCartItemVO> getMyShoppingCartItems(Long UserId) {
        List<ShoppingCartItemVO> shoppingCartItemVOs = new ArrayList<>();
        List<ShoppingCartItemEntity> shoppingCartItemEntities = shoppingCartItemMapper.selectByUserId(UserId, Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER);
        return getShoppingCartItemVOS(shoppingCartItemVOs, shoppingCartItemEntities);
    }

    @Override
    public List<ShoppingCartItemVO> getCartItemsForSettle(List<Long> cartItemIds, Long UserId) {
        List<ShoppingCartItemVO> shoppingCartItemVOs = new ArrayList<>();
        if (CollectionUtils.isEmpty(cartItemIds)) {
            MyException.fail("Shopping items cannot be empty");
        }
        List<ShoppingCartItemEntity> shoppingCartItemEntities = shoppingCartItemMapper.selectByUserIdAndCartItemIds(UserId, cartItemIds);
        if (CollectionUtils.isEmpty(shoppingCartItemEntities)) {
            MyException.fail("Shopping items cannot be empty");
        }
        if (shoppingCartItemEntities.size() != cartItemIds.size()) {
            MyException.fail("Parameter exception");
        }
        return getShoppingCartItemVOS(shoppingCartItemVOs, shoppingCartItemEntities);
    }

    /**
     * data convert
     *
     * @param shoppingCartItemVOs
     * @param shoppingCartItemEntities
     * @return
     */
    private List<ShoppingCartItemVO> getShoppingCartItemVOS(List<ShoppingCartItemVO> shoppingCartItemVOs, List<ShoppingCartItemEntity> shoppingCartItemEntities) {
        if (!CollectionUtils.isEmpty(shoppingCartItemEntities)) {
            //Query product information and do data conversion
            List<Long> mallGoodsIds = shoppingCartItemEntities.stream().map(ShoppingCartItemEntity::getGoodsId).collect(Collectors.toList());
            List<MallGoodsEntity> mallGoods = goodsMapper.selectByPrimaryKeys(mallGoodsIds);
            Map<Long, MallGoodsEntity> mallGoodsMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(mallGoods)) {
                mallGoodsMap = mallGoods.stream().collect(Collectors.toMap(MallGoodsEntity::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
            }
            for (ShoppingCartItemEntity shoppingCartItemEntity : shoppingCartItemEntities) {
                ShoppingCartItemVO shoppingCartItemVO = new ShoppingCartItemVO();
                BeanUtil.copyProperties(shoppingCartItemEntity, shoppingCartItemVO);
                if (mallGoodsMap.containsKey(shoppingCartItemEntity.getGoodsId())) {
                    MallGoodsEntity mallGoodsTemp = mallGoodsMap.get(shoppingCartItemEntity.getGoodsId());
                    shoppingCartItemVO.setGoodsCoverImg(mallGoodsTemp.getGoodsCoverImg());
                    String goodsName = mallGoodsTemp.getGoodsName();
                    // long string
                    if (goodsName.length() > 28) {
                        goodsName = goodsName.substring(0, 28) + "...";
                    }
                    shoppingCartItemVO.setGoodsName(goodsName);
                    shoppingCartItemVO.setSellingPrice(mallGoodsTemp.getSellingPrice());
                    shoppingCartItemVOs.add(shoppingCartItemVO);
                }
            }
        }
        return shoppingCartItemVOs;
    }

    @Override
    public PageResult getMyShoppingCartItems(PageQueryUtil pageUtil) {
        List<ShoppingCartItemVO> shoppingCartItemVOs = new ArrayList<>();
        List<ShoppingCartItemEntity> shoppingCartItemEntities = shoppingCartItemMapper.findMyCartItems(pageUtil);
        int total = shoppingCartItemMapper.getTotalMyCartItems(pageUtil);
        return new PageResult(getShoppingCartItemVOS(shoppingCartItemVOs, shoppingCartItemEntities), total, pageUtil.getLimit(), pageUtil.getPage());
    }
}
