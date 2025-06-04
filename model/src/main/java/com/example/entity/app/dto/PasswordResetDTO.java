package com.example.entity.app.dto;

import lombok.Data;

@Data
public class PasswordResetDTO {
    private String email;
    private String password;
    private String code;
}
