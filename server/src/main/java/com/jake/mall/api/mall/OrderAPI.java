
package com.jake.mall.api.mall;

import com.jake.mall.common.MyException;
import com.jake.mall.common.ServiceResultEnum;
import com.jake.mall.entity.MallUser;
import com.jake.mall.service.UserAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.jake.mall.api.mall.param.SaveOrderParam;
import com.jake.mall.api.mall.vo.OrderDetailVO;
import com.jake.mall.api.mall.vo.OrderListVO;
import com.jake.mall.common.Constants;
import com.jake.mall.config.annotation.TokenToMallUser;
import com.jake.mall.api.mall.vo.ShoppingCartItemVO;
import com.jake.mall.entity.MallUserAddress;
import com.jake.mall.service.OrderService;
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
@Api(value = "v1", tags = "Order Operation API")
@RequestMapping("/api/v1")
public class OrderAPI {

    @Resource
    private ShoppingCartService shoppingCartService;
    @Resource
    private OrderService orderService;
    @Resource
    private UserAddressService userAddressService;

    @PostMapping("/saveOrder")
    @ApiOperation(value = "Generate order API", notes = "The parameters are Array of address ids and shopping item ids.")
    public Result<Object> saveOrder(@ApiParam(value = "Order params") @RequestBody SaveOrderParam saveOrderParam, @TokenToMallUser MallUser loginMallUser) {
        int priceTotal = 0;
        if (saveOrderParam == null || saveOrderParam.getCartItemIds() == null || saveOrderParam.getAddressId() == null) {
            MyException.fail(ServiceResultEnum.PARAM_ERROR.getResult());
        }
        if (saveOrderParam.getCartItemIds().length < 1) {
            MyException.fail(ServiceResultEnum.PARAM_ERROR.getResult());
        }
        List<ShoppingCartItemVO> itemsForSave = shoppingCartService.getCartItemsForSettle(Arrays.asList(saveOrderParam.getCartItemIds()), loginMallUser.getUserId());
        if (CollectionUtils.isEmpty(itemsForSave)) {
            //No data
            MyException.fail("Parameter exceptions");
        } else {
            //Total price
            for (ShoppingCartItemVO shoppingCartItemVO : itemsForSave) {
                priceTotal += shoppingCartItemVO.getGoodsCount() * shoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                MyException.fail("Price exception");
            }
            MallUserAddress address = userAddressService.getMallUserAddressById(saveOrderParam.getAddressId());
            if (!loginMallUser.getUserId().equals(address.getUserId())) {
                return AppRes.error(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
            }
            //保存Order并返回OrderId
            String saveOrderResult = orderService.saveOrder(loginMallUser, address, itemsForSave);
            Result<Object> result = AppRes.ok();
            result.setData(saveOrderResult);
            return result;
        }
        return AppRes.error("Fail to generate order id.");
    }

    @GetMapping("/order/{orderNo}")
    @ApiOperation(value = "Order Details API", notes = "The parameters are OrderId")
    public Result<Object> orderDetailPage(@ApiParam(value = "OrderId") @PathVariable("orderNo") String orderNo, @TokenToMallUser MallUser loginMallUser) {
    //public Result<OrderDetailVO> orderDetailPage(@ApiParam(value = "OrderId") @PathVariable("orderNo") String orderNo, @TokenToMallUser MallUser loginMallUser) {
        return AppRes.ok(orderService.getOrderDetailByOrderNo(orderNo, loginMallUser.getUserId()));
    }

    @GetMapping("/order")
    @ApiOperation(value = "Order List API", notes = "The parameters are Page")
    public Result<Object> orderList(@ApiParam(value = "Page") @RequestParam(required = false) Integer pageNumber,
    //public Result<PageResult<List<OrderListVO>>> orderList(@ApiParam(value = "Page") @RequestParam(required = false) Integer pageNumber,
                                                           @ApiParam(value = "Order Status:0.To be paid 1.To be confirmed 2.To be shipped 3:Shipped 4.Transaction successful ") @RequestParam(required = false) Integer status,
                                                           @TokenToMallUser MallUser loginMallUser) {
        Map<String,Object> params = new HashMap<>(8);
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }
        params.put("userId", loginMallUser.getUserId());
        params.put("orderStatus", status);
        params.put("page", pageNumber);
        params.put("limit", Constants.ORDER_SEARCH_PAGE_LIMIT);
        //封装分页请求参数
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return AppRes.ok(orderService.getMyOrders(pageUtil));
    }

    @PutMapping("/order/{orderNo}/cancel")
    @ApiOperation(value = "Cancel order", notes = "The parameters are OrderId")
    public Result<Object> cancelOrder(@ApiParam(value = "OrderId") @PathVariable("orderNo") String orderNo, @TokenToMallUser MallUser loginMallUser) {
        String cancelOrderResult = orderService.cancelOrder(orderNo, loginMallUser.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(cancelOrderResult)) {
            return AppRes.ok();
        } else {
            return AppRes.error(cancelOrderResult);
        }
    }

    @PutMapping("/order/{orderNo}/finish")
    @ApiOperation(value = "Confirm received", notes = "The parameters are OrderId")
    public Result<Object> finishOrder(@ApiParam(value = "OrderId") @PathVariable("orderNo") String orderNo, @TokenToMallUser MallUser loginMallUser) {
        String finishOrderResult = orderService.finishOrder(orderNo, loginMallUser.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(finishOrderResult)) {
            return AppRes.ok();
        } else {
            return AppRes.error(finishOrderResult);
        }
    }

    @GetMapping("/paySuccess")
    @ApiOperation(value = "Interface to simulate a successful payment callback", notes = "The parameters are OrderId and payment method")
    public Result<Object> paySuccess(@ApiParam(value = "OrderId") @RequestParam("orderNo") String orderNo, @ApiParam(value = "Payment method") @RequestParam("payType") int payType) {
        String payResult = orderService.paySuccess(orderNo, payType);
        if (ServiceResultEnum.SUCCESS.getResult().equals(payResult)) {
            return AppRes.ok();
        } else {
            return AppRes.error(payResult);
        }
    }

}
