package com.example.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(name = "AuthorizeVO", description = "用户授权信息")
public class AuthorizeVO {
    @Schema(description = "用户名")
    String username;

    @Schema(description = "用户角色")
    String role;

    @Schema(description = "token")
    String token;

    @Schema(description = "token过期时间")
    Date expire;
}
