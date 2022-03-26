
package com.jake.mall.entity;

import lombok.Data;

@Data
public class OrderAddressEntity {
    private Long orderId;

    private String userName;

    private String userPhone;

    private String provinceName;

    private String cityName;

    private String regionName;

    private String detailAddress;
}
