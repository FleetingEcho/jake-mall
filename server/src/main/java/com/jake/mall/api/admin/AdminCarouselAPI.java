
package com.jake.mall.api.admin;

import com.jake.mall.api.admin.param.BatchIdParam;
import com.jake.mall.api.admin.param.CarouselAddParam;
import com.jake.mall.api.admin.param.CarouselEditParam;
import com.jake.mall.common.ServiceResultEnum;
import com.jake.mall.config.annotation.TokenToAdminUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.jake.mall.entity.AdminUserToken;
import com.jake.mall.entity.Carousel;
import com.jake.mall.service.CarouselService;
import com.jake.mall.util.BeanUtil;
import com.jake.mall.util.PageQueryUtil;
import com.jake.mall.util.Result;
import com.jake.mall.util.AppRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@RestController
@Api(value = "v1", tags = "Admin Carousel API")
@RequestMapping("/manage-api/v1")
public class AdminCarouselAPI {

    private static final Logger logger = LoggerFactory.getLogger(AdminCarouselAPI.class);

    @Resource
    CarouselService carouselService;

    /**
     *  List 
     */
    @RequestMapping(value = "/carousels", method = RequestMethod.GET)
    @ApiOperation(value = "Carousel  List ", notes = "Carousel  List ")
    public Result<Object> list(@RequestParam(required = false) @ApiParam(value = "page") Integer pageNumber,
                       @RequestParam(required = false) @ApiParam(value = "pageSize") Integer pageSize, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 10) {
            return AppRes.error("PaginationParameter exception!");
        }
        Map<String,Object> params = new HashMap<>(4);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return AppRes.ok(carouselService.getCarouselPage(pageUtil));
    }

    /**
     * add 
     */
    @RequestMapping(value = "/carousels", method = RequestMethod.POST)
    @ApiOperation(value = "add Carousel ", notes = "add Carousel ")
    public Result<Object> save(@RequestBody @Valid CarouselAddParam carouselAddParam, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        Carousel carousel = new Carousel();
        BeanUtil.copyProperties(carouselAddParam, carousel);
        String result = carouselService.saveCarousel(carousel);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return AppRes.ok();
        } else {
            return AppRes.error(result);
        }
    }


    /**
     * Modify 
     */
    @RequestMapping(value = "/carousels", method = RequestMethod.PUT)
    @ApiOperation(value = "Modify Carousel info ", notes = "Modify Carousel info ")
    public Result<Object> update(@RequestBody CarouselEditParam carouselEditParam, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        Carousel carousel = new Carousel();
        BeanUtil.copyProperties(carouselEditParam, carousel);
        String result = carouselService.updateCarousel(carousel);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return AppRes.ok();
        } else {
            return AppRes.error(result);
        }
    }

    /**
     *  detail
     */
    @RequestMapping(value = "/carousels/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get single Carousel info ", notes = "Query by id")
    public Result<Object> info(@PathVariable("id") Integer id, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        Carousel carousel = carouselService.getCarouselById(id);
        if (carousel == null) {
            return AppRes.error(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return AppRes.ok(carousel);
    }

    /**
     * Delete
     */
    @RequestMapping(value = "/carousels", method = RequestMethod.DELETE)
    @ApiOperation(value = "Batch DeleteCarousel info ", notes = "Batch DeleteCarousel info ")
    public Result<Object> delete(@RequestBody BatchIdParam batchIdParam, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        if (batchIdParam == null || batchIdParam.getIds().length < 1) {
            return AppRes.error("Parameter exception!");
        }
        if (carouselService.deleteBatch(batchIdParam.getIds())) {
            return AppRes.ok();
        } else {
            return AppRes.error("Fail to delete.");
        }
    }

}
