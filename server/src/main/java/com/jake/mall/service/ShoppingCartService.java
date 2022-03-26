
package com.jake.mall.service;

import com.jake.mall.api.mall.param.SaveCartItemParam;
import com.jake.mall.api.mall.param.UpdateCartItemParam;
import com.jake.mall.api.mall.vo.ShoppingCartItemVO;
import com.jake.mall.entity.ShoppingCartItemEntity;
import com.jake.mall.util.PageQueryUtil;
import com.jake.mall.util.PageResult;

import java.util.List;

public interface ShoppingCartService {

    /**
     * Save goods into the shopping cart
     *
     * @param saveCartItemParam
     * @param userId
     * @return
     */
    String saveCartItem(SaveCartItemParam saveCartItemParam, Long userId);

    /**
     * Modify the properties in your cart
     *
     * @param updateCartItemParam
     * @param userId
     * @return
     */
    String updateCartItem(UpdateCartItemParam updateCartItemParam, Long userId);

    /**
     * Get shopping item details
     *
     * @param ShoppingCartItemId
     * @return
     */
    ShoppingCartItemEntity getCartItemById(Long ShoppingCartItemId);

    /**
     * Delete goods in your cart
     *
     *
     * @param shoppingCartItemId
     * @param userId
     * @return
     */
    Boolean deleteById(Long shoppingCartItemId, Long userId);

    /**
     * Get list data in my shopping cart
     *
     * @param UserId
     * @return
     */
    List<ShoppingCartItemVO> getMyShoppingCartItems(Long UserId);

    /**
     * Get the corresponding shopping item record according to userid and cartitemids
     *
     * @param cartItemIds
     * @param UserId
     * @return
     */
    List<ShoppingCartItemVO> getCartItemsForSettle(List<Long> cartItemIds, Long UserId);

    /**
     * My shopping cart (paging data)
     *
     * @param pageUtil
     * @return
     */
    PageResult getMyShoppingCartItems(PageQueryUtil pageUtil);
}
