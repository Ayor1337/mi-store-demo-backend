package com.example.controller;

import com.example.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/info")
@Tag(name = "用户信息管理", description = "与用户信息相关的操作")
public class InfoController {

    @RequestMapping("/get_name")
    @Operation(summary = "获取登录用户名", description = "获取当前登录用户的用户名")
    public Result<String> getLoginUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Result.ok();
    }
}