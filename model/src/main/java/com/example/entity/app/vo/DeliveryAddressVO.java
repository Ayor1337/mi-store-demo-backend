package com.example.entity.app.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryAddressVO {

    private Integer id;
    private Integer userId;
    private String name;
    private String tag;
    private String address;
    private String phone;

}
