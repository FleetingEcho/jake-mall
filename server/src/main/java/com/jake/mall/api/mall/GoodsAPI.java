
package com.jake.mall.api.mall;

import com.jake.mall.api.mall.vo.GoodsDetailVO;
import com.jake.mall.api.mall.vo.SearchGoodsVO;
import com.jake.mall.common.Constants;
import com.jake.mall.common.MyException;
import com.jake.mall.common.ServiceResultEnum;
import com.jake.mall.config.annotation.TokenToMallUser;
import com.jake.mall.entity.MallUser;
import com.jake.mall.entity.MallGoodsEntity;
import com.jake.mall.service.GoodsService;
import com.jake.mall.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "v1", tags = "Shopping Item API")
@RequestMapping("/api/v1")
public class GoodsAPI {

    private static final Logger logger = LoggerFactory.getLogger(GoodsAPI.class);

    @Resource
    private GoodsService goodsService;

    @GetMapping("/search")
    @ApiOperation(value = "Shopping Item search API", notes = "Search by keyword and category id")
    //public Result<PageResult<List<SearchGoodsVO>>> search(@RequestParam(required = false) @ApiParam(value = "keyword") String keyword,
    public Result<Object> search(@RequestParam(required = false) @ApiParam(value = "Search by keyword") String keyword,
                                                          @RequestParam(required = false) @ApiParam(value = "category id") Long goodsCategoryId,
                                                          @RequestParam(required = false) @ApiParam(value = "orderBy") String orderBy,
                                                          @RequestParam(required = false) @ApiParam(value = "page") Integer pageNumber,
                                                          @TokenToMallUser MallUser loginMallUser) {

        logger.info("goods search api,keyword={},goodsCategoryId={},orderBy={},pageNumber={},userId={}", keyword, goodsCategoryId, orderBy, pageNumber, loginMallUser.getUserId());

        Map<String,Object> params = new HashMap<>(8);
        //Both search parameters are empty, return an exception directly
        if (goodsCategoryId == null && StringUtils.isEmpty(keyword)) {
            MyException.fail("Illegal search parameters");
        }
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }
        params.put("goodsCategoryId", goodsCategoryId);
        params.put("page", pageNumber);
        params.put("limit", Constants.GOODS_SEARCH_PAGE_LIMIT);
        //Filter on keyword to remove spaces
        if (!StringUtils.isEmpty(keyword)) {
            params.put("keyword", keyword);
        }
        if (!StringUtils.isEmpty(orderBy)) {
            params.put("orderBy", orderBy);
        }
        // Search the shelf status of Shopping Item
        params.put("goodsSellStatus", Constants.SELL_STATUS_UP);
        //Encapsulating Shopping Item data
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return AppRes.ok(goodsService.searchMallGoodsEntity(pageUtil));
    }

    @GetMapping("/goods/detail/{goodsId}")
    @ApiOperation(value = "Shopping Item Detail API", notes = "Parameter is Shopping Item id")
    public Result<Object> goodsDetail(@ApiParam(value = "Shopping Item id") @PathVariable("goodsId") Long goodsId, @TokenToMallUser MallUser loginMallUser) {
    //public Result<GoodsDetailVO> goodsDetail(@ApiParam(value = "Shopping Item id") @PathVariable("goodsId") Long goodsId, @TokenToMallUser MallUser loginMallUser) {
        logger.info("goods detail api,goodsId={},userId={}", goodsId, loginMallUser.getUserId());
        if (goodsId < 1) {
            return AppRes.error("");
        }
        MallGoodsEntity goods = goodsService.getMallGoodsEntityById(goodsId);
        if (Constants.SELL_STATUS_UP != goods.getGoodsSellStatus()) {
            MyException.fail(ServiceResultEnum.GOODS_PUT_DOWN.getResult());
        }
        GoodsDetailVO goodsDetailVO = new GoodsDetailVO();
        BeanUtil.copyProperties(goods, goodsDetailVO);
        goodsDetailVO.setGoodsCarouselList(goods.getGoodsCarousel().split(","));
        return AppRes.ok(goodsDetailVO);
    }

}
