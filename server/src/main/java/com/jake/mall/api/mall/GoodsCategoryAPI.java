
package com.jake.mall.api.mall;

import com.jake.mall.common.MyException;
import com.jake.mall.common.ServiceResultEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.jake.mall.api.mall.vo.IndexCategoryVO;
import com.jake.mall.service.CategoryService;
import com.jake.mall.util.Result;
import com.jake.mall.util.AppRes;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(value = "v1", tags = "Shopping Mall Categories API")
@RequestMapping("/api/v1")
public class GoodsCategoryAPI {

    @Resource
    private CategoryService categoryService;

    @GetMapping("/categories")
    @ApiOperation(value = "Access to classification data", notes = "Use of category pages")
    //public Result<List<IndexCategoryVO>> getCategories() {
    public Result<Object> getCategories() {
        List<IndexCategoryVO> categories = categoryService.getCategoriesForIndex();
        if (CollectionUtils.isEmpty(categories)) {
            MyException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return AppRes.ok(categories);
    }
}
