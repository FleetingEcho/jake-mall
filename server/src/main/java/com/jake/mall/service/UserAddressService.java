
package com.jake.mall.service;

import com.jake.mall.api.mall.vo.UserAddressVO;
import com.jake.mall.entity.MallUserAddress;

import java.util.List;

public interface UserAddressService {

    /**
     * get shipping address
     *
     * @param userId
     * @return
     */
    List<UserAddressVO> getMyAddresses(Long userId);

    /**
     * save shipping address
     *
     * @param mallUserAddress
     * @return
     */
    Boolean saveUserAddress(MallUserAddress mallUserAddress);

    /**
     * edit shipping address
     *
     * @param mallUserAddress
     * @return
     */
    Boolean updateMallUserAddress(MallUserAddress mallUserAddress);

    /**
     * shipping address info
     *
     * @param addressId
     * @return
     */
    MallUserAddress getMallUserAddressById(Long addressId);

    /**
     * get all my shipping addresses
     *
     * @param userId
     * @return
     */
    MallUserAddress getMyDefaultAddressByUserId(Long userId);

    /**
     * delete shipping address
     *
     * @param addressId
     * @return
     */
    Boolean deleteById(Long addressId);
}
