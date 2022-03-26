
package com.jake.mall.api.admin;

import com.jake.mall.api.admin.param.BatchIdParam;
import com.jake.mall.api.admin.param.IndexConfigAddParam;
import com.jake.mall.common.IndexConfigTypeEnum;
import com.jake.mall.common.ServiceResultEnum;
import com.jake.mall.config.annotation.TokenToAdminUser;
import com.jake.mall.service.IndexConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.jake.mall.api.admin.param.IndexConfigEditParam;
import com.jake.mall.entity.AdminUserToken;
import com.jake.mall.entity.IndexConfig;
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
@Api(value = "v1", tags = "Admin Config API")
@RequestMapping("/manage-api/v1")
public class AdminIndexConfigAPI {

    private static final Logger logger = LoggerFactory.getLogger(AdminIndexConfigAPI.class);

    @Resource
    private IndexConfigService indexConfigService;

    /**
     *  List
     */
    @RequestMapping(value = "/indexConfigs", method = RequestMethod.GET)
    @ApiOperation(value = "HomePage Config  List ", notes = "HomePage Config  List ")
    public Result<Object> list(@RequestParam(required = false) @ApiParam(value = "page") Integer pageNumber,
                       @RequestParam(required = false) @ApiParam(value = "pageSize") Integer pageSize,
                       @RequestParam(required = false) @ApiParam(value = "1- Search box hot search 2- Search drop down box hot search 3-(HomePage)Hot Shopping Item 4-(HomePage)New products online 5-(HomePage)Recommended for you") Integer configType, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 10) {
            return AppRes.error("PaginationParameter exception!");
        }
        IndexConfigTypeEnum indexConfigTypeEnum = IndexConfigTypeEnum.getIndexConfigTypeEnumByType(configType);
        if (indexConfigTypeEnum.equals(IndexConfigTypeEnum.DEFAULT)) {
            return AppRes.error("Invalid parameter！");
        }
        Map<String,Object> params = new HashMap<>(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        params.put("configType", configType);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return AppRes.ok(indexConfigService.getConfigsPage(pageUtil));
    }

    /**
     * add
     */
    @RequestMapping(value = "/indexConfigs", method = RequestMethod.POST)
    @ApiOperation(value = "add HomePage Config HomePage", notes = "add HomePage Config HomePage")
    public Result<Object> save(@RequestBody @Valid IndexConfigAddParam indexConfigAddParam, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        IndexConfig indexConfig = new IndexConfig();
        BeanUtil.copyProperties(indexConfigAddParam, indexConfig);
        String result = indexConfigService.saveIndexConfig(indexConfig);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return AppRes.ok();
        } else {
            return AppRes.error(result);
        }
    }


    /**
     * Modify
     */
    @RequestMapping(value = "/indexConfigs", method = RequestMethod.PUT)
    @ApiOperation(value = "Modify HomePage Config HomePage", notes = "Modify HomePage Config HomePage")
    public Result<Object> update(@RequestBody @Valid IndexConfigEditParam indexConfigEditParam, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        IndexConfig indexConfig = new IndexConfig();
        BeanUtil.copyProperties(indexConfigEditParam, indexConfig);
        String result = indexConfigService.updateIndexConfig(indexConfig);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return AppRes.ok();
        } else {
            return AppRes.error(result);
        }
    }

    /**
     *  detail
     */
    @RequestMapping(value = "/indexConfigs/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get single HomePage Config HomePageinfo ", notes = "Query by id")
    public Result<Object> info(@PathVariable("id") Long id, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        IndexConfig config = indexConfigService.getIndexConfigById(id);
        if (config == null) {
            return AppRes.error("No data found");
        }
        return AppRes.ok(config);
    }

    /**
     * Delete
     */
    @RequestMapping(value = "/indexConfigs", method = RequestMethod.DELETE)
    @ApiOperation(value = "Batch DeleteHomePage Config HomePageinfo ", notes = "Batch DeleteHomePage Config HomePageinfo ")
    public Result<Object> delete(@RequestBody BatchIdParam batchIdParam, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        if (batchIdParam == null || batchIdParam.getIds().length < 1) {
            return AppRes.error("Parameter exception!");
        }
        if (indexConfigService.deleteBatch(batchIdParam.getIds())) {
            return AppRes.ok();
        } else {
            return AppRes.error("Fail to delete.");
        }
    }

}
