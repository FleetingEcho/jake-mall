
package com.jake.mall.api.admin;

import com.jake.mall.api.admin.param.BatchIdParam;
import com.jake.mall.api.mall.vo.OrderDetailVO;
import com.jake.mall.common.ServiceResultEnum;
import com.jake.mall.config.annotation.TokenToAdminUser;
import com.jake.mall.entity.AdminUserToken;
import com.jake.mall.util.PageQueryUtil;
import com.jake.mall.util.Result;
import com.jake.mall.util.AppRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.jake.mall.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@RestController
@Api(value = "v1", tags = "Admin Orders API")
@RequestMapping("/manage-api/v1")
public class AdminOrderAPI {

    private static final Logger logger = LoggerFactory.getLogger(AdminOrderAPI.class);

    @Resource
    private OrderService orderService;

    /**
     *  List
     */
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    @ApiOperation(value = "Order List ", notes = "use useOrderId and Order Status filter")
    public Result<Object> list(@RequestParam(required = false) @ApiParam(value = "page") Integer pageNumber,
                       @RequestParam(required = false) @ApiParam(value = "pageSize") Integer pageSize,
                       @RequestParam(required = false) @ApiParam(value = "OrderId") String orderNo,
                       @RequestParam(required = false) @ApiParam(value = "Order Status") Integer orderStatus, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 10) {
            return AppRes.error("PaginationParameter exception!");
        }
        Map<String,Object> params = new HashMap<>(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        if (!StringUtils.isEmpty(orderNo)) {
            params.put("orderNo", orderNo);
        }
        if (orderStatus != null) {
            params.put("orderStatus", orderStatus);
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return AppRes.ok(orderService.getOrdersPage(pageUtil));
    }

    @GetMapping("/orders/{orderId}")
    @ApiOperation(value = "Order Details API", notes = "The parameters are OrderId")
    public Result<Object> orderDetailPage(@ApiParam(value = "OrderId") @PathVariable("orderId") Long orderId, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        return AppRes.ok(orderService.getOrderDetailByOrderId(orderId));
    }

    /**
     *Dispatch
     */
    @RequestMapping(value = "/orders/checkDone", method = RequestMethod.PUT)
    @ApiOperation(value = "Modify Order Status is dispatched", notes = "Batch Modify ")
    public Result<Object> checkDone(@RequestBody BatchIdParam batchIdParam, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        if (batchIdParam==null||batchIdParam.getIds().length < 1) {
            return AppRes.error("Parameter exception!");
        }
        String result = orderService.checkDone(batchIdParam.getIds());
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return AppRes.ok();
        } else {
            return AppRes.error(result);
        }
    }

    /**
     * Out of storage
     */
    @RequestMapping(value = "/orders/checkOut", method = RequestMethod.PUT)
    @ApiOperation(value = "Modify Order Status is out of storage", notes = "Batch Modify ")
    public Result<Object> checkOut(@RequestBody BatchIdParam batchIdParam, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        if (batchIdParam==null||batchIdParam.getIds().length < 1) {
            return AppRes.error("Parameter exception!");
        }
        String result = orderService.checkOut(batchIdParam.getIds());
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return AppRes.ok();
        } else {
            return AppRes.error(result);
        }
    }

    /**
     * 关闭Order
     */
    @RequestMapping(value = "/orders/close", method = RequestMethod.PUT)
    @ApiOperation(value = "Modify Order Status shop closed.", notes = "Batch Modify ")
    public Result<Object> closeOrder(@RequestBody BatchIdParam batchIdParam, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        if (batchIdParam==null||batchIdParam.getIds().length < 1) {
            return AppRes.error("Parameter exception!");
        }
        String result = orderService.closeOrder(batchIdParam.getIds());
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return AppRes.ok();
        } else {
            return AppRes.error(result);
        }
    }
}
