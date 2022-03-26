
package com.jake.mall.api.mall;

import com.jake.mall.api.mall.param.SaveMallUserAddressParam;
import com.jake.mall.api.mall.param.UpdateMallUserAddressParam;
import com.jake.mall.api.mall.vo.UserAddressVO;
import com.jake.mall.common.ServiceResultEnum;
import com.jake.mall.entity.MallUser;
import com.jake.mall.service.UserAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.jake.mall.config.annotation.TokenToMallUser;
import com.jake.mall.entity.MallUserAddress;
import com.jake.mall.util.BeanUtil;
import com.jake.mall.util.Result;
import com.jake.mall.util.AppRes;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(value = "v1", tags = "User Address API")
@RequestMapping("/api/v1")
public class UserAddressAPI {

    @Resource
    private UserAddressService mallUserAddressService;

    @GetMapping("/address")
    @ApiOperation(value = "List of my delivery addresses", notes = "")
    public Result<Object> addressList(@TokenToMallUser MallUser loginMallUser) {
        return AppRes.ok(mallUserAddressService.getMyAddresses(loginMallUser.getUserId()));
    }

    @PostMapping("/address")
    @ApiOperation(value = "Add address", notes = "")
    public Result<Object> saveUserAddress(@RequestBody SaveMallUserAddressParam saveMallUserAddressParam,
                                           @TokenToMallUser MallUser loginMallUser) {
        MallUserAddress userAddress = new MallUserAddress();
        BeanUtil.copyProperties(saveMallUserAddressParam, userAddress);
        userAddress.setUserId(loginMallUser.getUserId());
        Boolean saveResult = mallUserAddressService.saveUserAddress(userAddress);
        //success
        if (saveResult) {
            return AppRes.ok();
        }
        //fail
        return AppRes.error("Failed to add");
    }

    @PutMapping("/address")
    @ApiOperation(value = "Change of address", notes = "")
    public Result<Object> updateMallUserAddress(@RequestBody UpdateMallUserAddressParam updateMallUserAddressParam,
                                                 @TokenToMallUser MallUser loginMallUser) {
        MallUserAddress mallUserAddressById = mallUserAddressService.getMallUserAddressById(updateMallUserAddressParam.getAddressId());
        if (!loginMallUser.getUserId().equals(mallUserAddressById.getUserId())) {
            return AppRes.error(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        MallUserAddress userAddress = new MallUserAddress();
        BeanUtil.copyProperties(updateMallUserAddressParam, userAddress);
        userAddress.setUserId(loginMallUser.getUserId());
        Boolean updateResult = mallUserAddressService.updateMallUserAddress(userAddress);
        //success
        if (updateResult) {
            return AppRes.ok();
        }
        //fail
        return AppRes.error("Modification failed");
    }

    @GetMapping("/address/{addressId}")
    @ApiOperation(value = "Get delivery address details", notes = "The parameters are address id")
    public Result<Object> getMallUserAddress(@PathVariable("addressId") Long addressId,
                                                    @TokenToMallUser MallUser loginMallUser) {
        MallUserAddress mallUserAddressById = mallUserAddressService.getMallUserAddressById(addressId);
        UserAddressVO userAddressVO = new UserAddressVO();
        BeanUtil.copyProperties(mallUserAddressById, userAddressVO);
        if (!loginMallUser.getUserId().equals(mallUserAddressById.getUserId())) {
            return AppRes.error(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        return AppRes.ok(userAddressVO);
    }

    @GetMapping("/address/default")
    @ApiOperation(value = "Get default delivery address", notes = "")
    public Result<Object> getDefaultMallUserAddress(@TokenToMallUser MallUser loginMallUser) {
        MallUserAddress mallUserAddressById = mallUserAddressService.getMyDefaultAddressByUserId(loginMallUser.getUserId());
        return AppRes.ok(mallUserAddressById);
    }

    @DeleteMapping("/address/{addressId}")
    @ApiOperation(value = "Delete delivery address", notes = "The parameters are address id")
    public Result<Object> deleteAddress(@PathVariable("addressId") Long addressId,
                                @TokenToMallUser MallUser loginMallUser) {
        MallUserAddress mallUserAddressById = mallUserAddressService.getMallUserAddressById(addressId);
        if (!loginMallUser.getUserId().equals(mallUserAddressById.getUserId())) {
            return AppRes.error(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        Boolean deleteResult = mallUserAddressService.deleteById(addressId);
        //success
        if (deleteResult) {
            return AppRes.ok();
        }
        //Fail to delete.
        return AppRes.error(ServiceResultEnum.OPERATE_ERROR.getResult());
    }
}
