package com.example.entity.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryAddressDTO {
    private Integer id;
    private Integer userId;
    private String name;
    private String tag;
    private String address;
    private String phone;
    private Boolean isDeleted;
}
