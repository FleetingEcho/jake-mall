
package com.jake.mall.api.admin;

import com.jake.mall.api.admin.param.BatchIdParam;
import com.jake.mall.config.annotation.TokenToAdminUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.jake.mall.entity.AdminUserToken;
import com.jake.mall.service.UserService;
import com.jake.mall.util.PageQueryUtil;
import com.jake.mall.util.Result;
import com.jake.mall.util.AppRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@RestController
@Api(value = "v1", tags = "Admin User Register")
@RequestMapping("/manage-api/v1")
public class AdminRegisteUserAPI {

    private static final Logger logger = LoggerFactory.getLogger(AdminRegisteUserAPI.class);

    @Resource
    private UserService userService;

    /**
     *  List
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ApiOperation(value = "Registered user List ", notes = "Registered user List ")
    public Result<Object> list(@RequestParam(required = false) @ApiParam(value = "page") Integer pageNumber,
                       @RequestParam(required = false) @ApiParam(value = "pageSize") Integer pageSize,
                       @RequestParam(required = false) @ApiParam(value = "User status") Integer lockStatus, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 10) {
            return AppRes.error("Parameter exception!");
        }
        Map<String,Object> params = new HashMap<>(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        if (lockStatus != null) {
            params.put("orderStatus", lockStatus);
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return AppRes.ok(userService.getUsersPage(pageUtil));
    }

    /**
     * User disable and disable (0 - unlocked 1 - locked)
     */
    @RequestMapping(value = "/users/{lockStatus}", method = RequestMethod.PUT)
    @ApiOperation(value = "Modify User status", notes = "Batch Modify, User disable and disable (0 - unlocked 1 - locked)")
    public Result<Object> lockUser(@RequestBody BatchIdParam batchIdParam, @PathVariable int lockStatus, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        if (batchIdParam==null||batchIdParam.getIds().length < 1) {
            return AppRes.error("Parameter exception!");
        }
        if (lockStatus != 0 && lockStatus != 1) {
            return AppRes.error("Invalid Operation");
        }
        if (userService.lockUsers(batchIdParam.getIds(), lockStatus)) {
            return AppRes.ok();
        } else {
            return AppRes.error("Prohibit failed.");
        }
    }
}
