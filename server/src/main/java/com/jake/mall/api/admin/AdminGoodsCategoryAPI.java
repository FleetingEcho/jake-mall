
package com.jake.mall.api.admin;

import com.jake.mall.api.admin.param.BatchIdParam;
import com.jake.mall.api.admin.param.GoodsCategoryAddParam;
import com.jake.mall.common.CategoryLevelEnum;
import com.jake.mall.common.ServiceResultEnum;
import com.jake.mall.config.annotation.TokenToAdminUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.jake.mall.api.admin.param.GoodsCategoryEditParam;
import com.jake.mall.entity.AdminUserToken;
import com.jake.mall.entity.GoodsCategory;
import com.jake.mall.service.CategoryService;
import com.jake.mall.util.BeanUtil;
import com.jake.mall.util.PageQueryUtil;
import com.jake.mall.util.Result;
import com.jake.mall.util.AppRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@Api(value = "v1", tags = "Admin Category API")
@RequestMapping("/manage-api/v1")
public class AdminGoodsCategoryAPI {

    private static final Logger logger = LoggerFactory.getLogger(AdminGoodsCategoryAPI.class);

    @Resource
    private CategoryService categoryService;

    /**
     *  List
     */
    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    @ApiOperation(value = "Shopping ItemCategory  List ", notes = "use level and parent category id to query")
    public Result<Object> list(@RequestParam(required = false) @ApiParam(value = "page") Integer pageNumber,
                       @RequestParam(required = false) @ApiParam(value = "pageSize") Integer pageSize,
                       @RequestParam(required = false) @ApiParam(value = "Category level") Integer categoryLevel,
                       @RequestParam(required = false) @ApiParam(value = "Parent Category id") Long parentId, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 10 || categoryLevel == null || categoryLevel < 0 || categoryLevel > 3 || parentId == null || parentId < 0) {
            return AppRes.error("PaginationParameter exception!");
        }
        Map<String,Object> params = new HashMap<>(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        params.put("categoryLevel", categoryLevel);
        params.put("parentId", parentId);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return AppRes.ok(categoryService.getCategorisPage(pageUtil));
    }

    /**
     *  List
     */
    @RequestMapping(value = "/categories4Select", method = RequestMethod.GET)
    @ApiOperation(value = "Shopping ItemCategory  List ", notes = "For the production of three-level Category linkage effects")
    public Result<Object> listForSelect(@RequestParam("categoryId") Long categoryId, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        if (categoryId == null || categoryId < 1) {
            return AppRes.error("Parameter missing！");
        }
        GoodsCategory category = categoryService.getGoodsCategoryById(categoryId);
        //If it is neither a primary category nor a secondary category, then no data is returned.
        if (category == null || category.getCategoryLevel() == CategoryLevelEnum.LEVEL_THREE.getLevel()) {
            return AppRes.error("Parameter exception!");
        }
        Map<String,Object> categoryResult = new HashMap<>(4);
        if (category.getCategoryLevel() == CategoryLevelEnum.LEVEL_ONE.getLevel()) {
            //If it is a Level 1 Category, it returns all Level 2 Categories under the current Level 1 Category, and all Level 3 Category Lists under the first data in the Level 2 Category List.
            //Query all secondary categories of the first entity in the primary Category List
            List<GoodsCategory> secondLevelCategories = categoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(categoryId), CategoryLevelEnum.LEVEL_TWO.getLevel());
            if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                //Query all Level 3 Categories for the first entity in the Level 2 Category List
                List<GoodsCategory> thirdLevelCategories = categoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), CategoryLevelEnum.LEVEL_THREE.getLevel());
                categoryResult.put("secondLevelCategories", secondLevelCategories);
                categoryResult.put("thirdLevelCategories", thirdLevelCategories);
            }
        }
        if (category.getCategoryLevel() == CategoryLevelEnum.LEVEL_TWO.getLevel()) {
            //If it is a secondary Category then it returns all the tertiary Category Lists under the current Category
            List<GoodsCategory> thirdLevelCategories = categoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(categoryId), CategoryLevelEnum.LEVEL_THREE.getLevel());
            categoryResult.put("thirdLevelCategories", thirdLevelCategories);
        }
        return AppRes.ok(categoryResult);
    }

    /**
     * add
     */
    @RequestMapping(value = "/categories", method = RequestMethod.POST)
    @ApiOperation(value = "add Category ", notes = "add Category ")
    public Result<Object> save(@RequestBody @Valid GoodsCategoryAddParam goodsCategoryAddParam, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        GoodsCategory goodsCategory = new GoodsCategory();
        BeanUtil.copyProperties(goodsCategoryAddParam, goodsCategory);
        String result = categoryService.saveCategory(goodsCategory);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return AppRes.ok();
        } else {
            return AppRes.error(result);
        }
    }


    /**
     * Modify
     */
    @RequestMapping(value = "/categories", method = RequestMethod.PUT)
    @ApiOperation(value = "Modify Category info ", notes = "Modify Category info ")
    public Result<Object> update(@RequestBody @Valid GoodsCategoryEditParam goodsCategoryEditParam, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        GoodsCategory goodsCategory = new GoodsCategory();
        BeanUtil.copyProperties(goodsCategoryEditParam, goodsCategory);
        String result = categoryService.updateGoodsCategory(goodsCategory);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return AppRes.ok();
        } else {
            return AppRes.error(result);
        }
    }

    /**
     *  detail
     */
    @RequestMapping(value = "/categories/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get single Category info ", notes = "Query by id")
    public Result<Object> info(@PathVariable("id") Long id, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        GoodsCategory goodsCategory = categoryService.getGoodsCategoryById(id);
        if (goodsCategory == null) {
            return AppRes.error("No data found");
        }
        return AppRes.ok(goodsCategory);
    }

    /**
     * Category Delete
     */
    @RequestMapping(value = "/categories", method = RequestMethod.DELETE)
    @ApiOperation(value = "Batch DeleteCategory info ", notes = "Batch DeleteCategory info ")
    public Result<Object> delete(@RequestBody BatchIdParam batchIdParam, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        if (batchIdParam == null || batchIdParam.getIds().length < 1) {
            return AppRes.error("Parameter exception!");
        }
        if (categoryService.deleteBatch(batchIdParam.getIds())) {
            return AppRes.ok();
        } else {
            return AppRes.error("Fail to delete.");
        }
    }
}
