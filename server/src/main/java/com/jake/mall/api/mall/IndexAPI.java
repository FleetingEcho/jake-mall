
package com.jake.mall.api.mall;

import com.jake.mall.api.mall.vo.IndexCarouselVO;
import com.jake.mall.api.mall.vo.IndexConfigGoodsVO;
import com.jake.mall.api.mall.vo.IndexInfoVO;
import com.jake.mall.common.Constants;
import com.jake.mall.common.IndexConfigTypeEnum;
import com.jake.mall.service.IndexConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.jake.mall.service.CarouselService;
import com.jake.mall.util.Result;
import com.jake.mall.util.AppRes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(value = "v1", tags = "HomePage API")
@RequestMapping("/api/v1")
public class IndexAPI {

    @Resource
    private CarouselService carouselService;

    @Resource
    private IndexConfigService indexConfigService;

    @GetMapping("/index-infos")
    @ApiOperation(value = "Get home page data", notes = "Rotating images, new products, testimonials, etc. ")
    public Result<Object> indexInfo() {
        IndexInfoVO indexInfoVO = new IndexInfoVO();
        List<IndexCarouselVO> carousels = carouselService.getCarouselsForIndex(Constants.INDEX_CAROUSEL_NUMBER);
        List<IndexConfigGoodsVO> hotGoodses = indexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), Constants.INDEX_GOODS_HOT_NUMBER);
        List<IndexConfigGoodsVO> newGoodses = indexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(), Constants.INDEX_GOODS_NEW_NUMBER);
        List<IndexConfigGoodsVO> recommendGoodses = indexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND.getType(), Constants.INDEX_GOODS_RECOMMEND_NUMBER);
        indexInfoVO.setCarousels(carousels);
        indexInfoVO.setHotGoodses(hotGoodses);
        indexInfoVO.setNewGoodses(newGoodses);
        indexInfoVO.setRecommendGoodses(recommendGoodses);
        return AppRes.ok(indexInfoVO);
    }
}
