package com.example.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountVO {
    private Integer userId;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;
}
