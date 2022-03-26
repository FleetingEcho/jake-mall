
package com.jake.mall.api.admin;

import com.jake.mall.api.admin.param.BatchIdParam;
import com.jake.mall.api.admin.param.GoodsAddParam;
import com.jake.mall.api.admin.param.GoodsEditParam;
import com.jake.mall.common.Constants;
import com.jake.mall.common.ServiceResultEnum;
import com.jake.mall.config.annotation.TokenToAdminUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.jake.mall.entity.AdminUserToken;
import com.jake.mall.entity.GoodsCategory;
import com.jake.mall.entity.MallGoodsEntity;
import com.jake.mall.service.CategoryService;
import com.jake.mall.service.GoodsService;
import com.jake.mall.util.BeanUtil;
import com.jake.mall.util.PageQueryUtil;
import com.jake.mall.util.Result;
import com.jake.mall.util.AppRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@RestController
@Api(value = "v1", tags = "Admin Shopping Item API")
@RequestMapping("/manage-api/v1")
public class AdminGoodsInfoAPI {

    private static final Logger logger = LoggerFactory.getLogger(AdminGoodsInfoAPI.class);

    @Resource
    private GoodsService goodsService;
    @Resource
    private CategoryService categoryService;

    /**
     *  List
     */
    @RequestMapping(value = "/goods/list", method = RequestMethod.GET)
    @ApiOperation(value = "Shopping Item List ", notes = "use Name go to shelves status to filter")
    public Result<Object> list(@RequestParam(required = false) @ApiParam(value = "page") Integer pageNumber,
                       @RequestParam(required = false) @ApiParam(value = "pageSize") Integer pageSize,
                       @RequestParam(required = false) @ApiParam(value = "Shopping Item Name") String goodsName,
                       @RequestParam(required = false) @ApiParam(value = "Go to shelves status 0-go 1-off") Integer goodsSellStatus, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 10) {
            return AppRes.error("PaginationParameter exception!");
        }
        Map<String,Object> params = new HashMap<>(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        if (!StringUtils.isEmpty(goodsName)) {
            params.put("goodsName", goodsName);
        }
        if (goodsSellStatus != null) {
            params.put("goodsSellStatus", goodsSellStatus);
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return AppRes.ok(goodsService.getMallGoodsEntityPage(pageUtil));
    }

    /**
     * add
     */
    @RequestMapping(value = "/goods", method = RequestMethod.POST)
    @ApiOperation(value = "add Shopping Item info ", notes = "add Shopping Item info ")
    public Result<Object> save(@RequestBody @Valid GoodsAddParam goodsAddParam, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        MallGoodsEntity mallGoods = new MallGoodsEntity();
        BeanUtil.copyProperties(goodsAddParam, mallGoods);
        String result = goodsService.saveMallGoodsEntity(mallGoods);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return AppRes.ok();
        } else {
            return AppRes.error(result);
        }
    }


    /**
     * Modify
     */
    @RequestMapping(value = "/goods", method = RequestMethod.PUT)
    @ApiOperation(value = "Modify Shopping Item info ", notes = "Modify Shopping Item info ")
    public Result<Object> update(@RequestBody @Valid GoodsEditParam goodsEditParam, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        MallGoodsEntity mallGoods = new MallGoodsEntity();
        BeanUtil.copyProperties(goodsEditParam, mallGoods);
        String result = goodsService.updateMallGoodsEntity(mallGoods);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return AppRes.ok();
        } else {
            return AppRes.error(result);
        }
    }

    /**
     *  detail
     */
    @GetMapping("/goods/{id}")
    @ApiOperation(value = "Get single Shopping Item info ", notes = "Query by id")
    public Result<Object> info(@PathVariable("id") Long id, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        Map<String,Object> goodsInfo = new HashMap<>(8);
        MallGoodsEntity goods = goodsService.getMallGoodsEntityById(id);
        if (goods == null) {
            return AppRes.error(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        goodsInfo.put("goods", goods);
        GoodsCategory thirdCategory;
        GoodsCategory secondCategory;
        GoodsCategory firstCategory;
        thirdCategory = categoryService.getGoodsCategoryById(goods.getGoodsCategoryId());
        if (thirdCategory != null) {
            goodsInfo.put("thirdCategory", thirdCategory);
            secondCategory = categoryService.getGoodsCategoryById(thirdCategory.getParentId());
            if (secondCategory != null) {
                goodsInfo.put("secondCategory", secondCategory);
                firstCategory = categoryService.getGoodsCategoryById(secondCategory.getParentId());
                if (firstCategory != null) {
                    goodsInfo.put("firstCategory", firstCategory);
                }
            }
        }
        return AppRes.ok(goodsInfo);
    }

    /**
     * Batch Modify sell status
     */
    @RequestMapping(value = "/goods/status/{sellStatus}", method = RequestMethod.PUT)
    @ApiOperation(value = "Batch Modify sell status", notes = "Batch Modify sell status")
    public Result<Object> delete(@RequestBody BatchIdParam batchIdParam, @PathVariable("sellStatus") int sellStatus, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        if (batchIdParam == null || batchIdParam.getIds().length < 1) {
            return AppRes.error("Parameter exception!");
        }
        if (sellStatus != Constants.SELL_STATUS_UP && sellStatus != Constants.SELL_STATUS_DOWN) {
            return AppRes.error(" status exception！");
        }
        if (goodsService.batchUpdateSellStatus(batchIdParam.getIds(), sellStatus)) {
            return AppRes.ok();
        } else {
            return AppRes.error("Modify failed");
        }
    }

}
