
package com.jake.mall.dao;

import com.jake.mall.util.PageQueryUtil;
import com.jake.mall.entity.ShoppingCartItemEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface ShoppingCartItemMapper {
    int deleteByPrimaryKey(Long cartItemId);

    int insert(ShoppingCartItemEntity record);

    int insertSelective(ShoppingCartItemEntity record);

    ShoppingCartItemEntity selectByPrimaryKey(Long cartItemId);

    ShoppingCartItemEntity selectByUserIdAndGoodsId(@Param("UserId") Long UserId, @Param("goodsId") Long goodsId);

    List<ShoppingCartItemEntity> selectByUserId(@Param("UserId") Long UserId, @Param("number") int number);

    List<ShoppingCartItemEntity> selectByUserIdAndCartItemIds(@Param("UserId") Long UserId, @Param("cartItemIds") List<Long> cartItemIds);

    int selectCountByUserId(Long UserId);

    int updateByPrimaryKeySelective(ShoppingCartItemEntity record);

    int updateByPrimaryKey(ShoppingCartItemEntity record);

    int deleteBatch(List<Long> ids);

    List<ShoppingCartItemEntity> findMyCartItems(PageQueryUtil pageUtil);

    int getTotalMyCartItems(PageQueryUtil pageUtil);
}
