
package com.jake.mall.api.mall;

import com.jake.mall.entity.MallUser;
import com.jake.mall.util.BeanUtil;
import com.jake.mall.util.NumberUtil;
import com.jake.mall.util.Result;
import com.jake.mall.util.AppRes;
import io.swagger.annotations.*;
import com.jake.mall.api.mall.param.MallUserLoginParam;
import com.jake.mall.api.mall.param.MallUserRegisterParam;
import com.jake.mall.api.mall.param.MallUserUpdateParam;
import com.jake.mall.common.Constants;
import com.jake.mall.common.ServiceResultEnum;
import com.jake.mall.config.annotation.TokenToMallUser;
import com.jake.mall.api.mall.vo.UserVO;
import com.jake.mall.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@Api(value = "v1", tags = "User Operation API")
@RequestMapping("/api/v1")
public class PersonalAPI {

    @Resource
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(PersonalAPI.class);

    @PostMapping("/user/login")
    @ApiOperation(value = "Login", notes = "return token")
    public Result<Object> login(@RequestBody @Valid MallUserLoginParam mallUserLoginParam) {
        if (!NumberUtil.isPhone(mallUserLoginParam.getLoginName())){
            return AppRes.error(ServiceResultEnum.LOGIN_NAME_IS_NOT_PHONE.getResult());
        }
        String loginResult = userService.login(mallUserLoginParam.getLoginName(), mallUserLoginParam.getPasswordMd5());

        logger.info("login api,loginName={},loginResult={}", mallUserLoginParam.getLoginName(), loginResult);

        //success
        if (!StringUtils.isEmpty(loginResult) && loginResult.length() == Constants.TOKEN_LENGTH) {
            Result<Object> result = AppRes.ok();
            result.setData(loginResult);
            return result;
        }
        //fail
        return AppRes.error(loginResult);
    }


    @PostMapping("/user/logout")
    @ApiOperation(value = "Logout", notes = "clear token")
    public Result<Object> logout(@TokenToMallUser MallUser loginMallUser) {
        Boolean logoutResult = userService.logout(loginMallUser.getUserId());

        logger.info("logout api,loginMallUser={}", loginMallUser.getUserId());

        //success
        if (logoutResult) {
            return AppRes.ok();
        }
        //failed
        return AppRes.error("logout error");
    }


    @PostMapping("/user/register")
    @ApiOperation(value = "Register", notes = "")
    public Result<Object> register(@RequestBody @Valid MallUserRegisterParam mallUserRegisterParam) {
        if (!NumberUtil.isPhone(mallUserRegisterParam.getLoginName())){
            return AppRes.error(ServiceResultEnum.LOGIN_NAME_IS_NOT_PHONE.getResult());
        }
        String registerResult = userService.register(mallUserRegisterParam.getLoginName(), mallUserRegisterParam.getPassword());

        logger.info("register api,loginName={},loginResult={}", mallUserRegisterParam.getLoginName(), registerResult);

        //success
        if (ServiceResultEnum.SUCCESS.getResult().equals(registerResult)) {
            return AppRes.ok();
        }
        //failed
        return AppRes.error(registerResult);
    }

    @PutMapping("/user/info")
    @ApiOperation(value = "Modify user information", notes = "")
    public Result<Object> updateInfo(@RequestBody @ApiParam("user information") MallUserUpdateParam mallUserUpdateParam, @TokenToMallUser MallUser loginMallUser) {
        Boolean flag = userService.updateUserInfo(mallUserUpdateParam, loginMallUser.getUserId());
        if (flag) {
            //success
            return  AppRes.ok();
        } else {
            //failed
            return  AppRes.error("Modification failed");
        }
    }

    @GetMapping("/user/info")
    @ApiOperation(value = "Get user info", notes = "")
    public Result<Object> getUserDetail(@TokenToMallUser MallUser loginMallUser) {
        //already logged in , return infp
        UserVO mallUserVO = new UserVO();
        BeanUtil.copyProperties(loginMallUser, mallUserVO);
        return AppRes.ok(mallUserVO);
    }
}
