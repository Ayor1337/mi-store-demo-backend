package com.example.entity.app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRegisterDTO {

    private String username;

    private String password;

    private String email;

    private String phone;

    
}
