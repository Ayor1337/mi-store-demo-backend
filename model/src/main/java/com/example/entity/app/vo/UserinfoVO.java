package com.example.entity.app.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserinfoVO {
    private Integer userId;
    private String username;
    private String avatarUrl;
    private String email;
    private String phone;
}
