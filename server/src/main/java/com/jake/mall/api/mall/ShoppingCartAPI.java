
package com.jake.mall.api.mall;

import com.jake.mall.entity.MallUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.jake.mall.api.mall.param.SaveCartItemParam;
import com.jake.mall.api.mall.param.UpdateCartItemParam;
import com.jake.mall.common.Constants;
import com.jake.mall.common.MyException;
import com.jake.mall.common.ServiceResultEnum;
import com.jake.mall.config.annotation.TokenToMallUser;
import com.jake.mall.api.mall.vo.ShoppingCartItemVO;
import com.jake.mall.entity.ShoppingCartItemEntity;
import com.jake.mall.service.ShoppingCartService;
import com.jake.mall.util.PageQueryUtil;
import com.jake.mall.util.PageResult;
import com.jake.mall.util.Result;
import com.jake.mall.util.AppRes;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "v1", tags = "Shopping Cart Operations API")
@RequestMapping("/api/v1")
public class ShoppingCartAPI {

    @Resource
    private ShoppingCartService shoppingCartService;

    @GetMapping("/shop-cart/page")
    @ApiOperation(value = "Shopping cart list (default 5 items per page)", notes = "The parameters are page")
    public Result<Object > cartItemPageList(Integer pageNumber, @TokenToMallUser MallUser loginMallUser) {
        Map<String,Object> params = new HashMap<>(8);
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }
        params.put("userId", loginMallUser.getUserId());
        params.put("page", pageNumber);
        params.put("limit", Constants.SHOPPING_CART_PAGE_LIMIT);
        //Encapsulating paging request parameters
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return AppRes.ok(shoppingCartService.getMyShoppingCartItems(pageUtil));
    }

    @GetMapping("/shop-cart")
    @ApiOperation(value = "Shopping cart list (web mobile without pagination)", notes = "")
    public Result<Object > cartItemList(@TokenToMallUser MallUser loginMallUser) {
        return AppRes.ok(shoppingCartService.getMyShoppingCartItems(loginMallUser.getUserId()));
    }

    @PostMapping("/shop-cart")
    @ApiOperation(value = "Add Shopping Item to Cart API", notes = "The parameters are Shopping Item id、count")
    public Result<Object> saveShoppingCartItem(@RequestBody SaveCartItemParam saveCartItemParam,
                                                 @TokenToMallUser MallUser loginMallUser) {
        String saveResult = shoppingCartService.saveCartItem(saveCartItemParam, loginMallUser.getUserId());
        //success
        if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
            return AppRes.ok();
        }
        //Fail
        return AppRes.error(saveResult);
    }

    @PutMapping("/shop-cart")
    @ApiOperation(value = "Modify shopping item data", notes = "The parameters are shopping id、count")
    public Result<Object> updateShoppingCartItem(@RequestBody UpdateCartItemParam updateCartItemParam,
                                                   @TokenToMallUser MallUser loginMallUser) {
        String updateResult = shoppingCartService.updateCartItem(updateCartItemParam, loginMallUser.getUserId());
        //success
        if (ServiceResultEnum.SUCCESS.getResult().equals(updateResult)) {
            return AppRes.ok();
        }
        //Fail
        return AppRes.error(updateResult);
    }

    @DeleteMapping("/shop-cart/{ShoppingCartItemId}")
    @ApiOperation(value = "delete shopping item", notes = "The parameters are shopping item id")
    public Result<Object> updateShoppingCartItem(@PathVariable("ShoppingCartItemId") Long ShoppingCartItemId,
                                                   @TokenToMallUser MallUser loginMallUser) {
        ShoppingCartItemEntity CartItemById = shoppingCartService.getCartItemById(ShoppingCartItemId);
        if (!loginMallUser.getUserId().equals(CartItemById.getUserId())) {
            return AppRes.error(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        Boolean deleteResult = shoppingCartService.deleteById(ShoppingCartItemId,loginMallUser.getUserId());
        //success
        if (deleteResult) {
            return AppRes.ok();
        }
        //Fail to delete.
        return AppRes.error(ServiceResultEnum.OPERATE_ERROR.getResult());
    }

    @GetMapping("/shop-cart/settle")
    @ApiOperation(value = "Search for item details based on an array of shopping item ids", notes = "Use the order confirmation page")
    public Result<Object> toSettle(Long[] cartItemIds, @TokenToMallUser MallUser loginMallUser) {
        if (cartItemIds.length < 1) {
            MyException.fail("Parameter exceptions");
        }
        int priceTotal = 0;
        List<ShoppingCartItemVO> itemsForSettle = shoppingCartService.getCartItemsForSettle(Arrays.asList(cartItemIds), loginMallUser.getUserId());
        if (CollectionUtils.isEmpty(itemsForSettle)) {
            //Throw an exception if there is no data
            MyException.fail("Parameter exceptions");
        } else {
            //total price
            for (ShoppingCartItemVO shoppingCartItemVO : itemsForSettle) {
                priceTotal += shoppingCartItemVO.getGoodsCount() * shoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                MyException.fail("Price exception");
            }
        }
        return AppRes.ok(itemsForSettle);
    }
}
