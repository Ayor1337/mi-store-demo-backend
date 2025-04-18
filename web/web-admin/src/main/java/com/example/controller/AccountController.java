package com.example.controller;

import com.example.entity.dto.AccountDTO;
import com.example.entity.vo.AccountVO;
import com.example.result.Result;
import com.example.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Tag(name = "用户管理", description = "与用户相关的操作")
public class AccountController {

    @Resource
    private AccountService accountService;

    @GetMapping("/list")
    @Operation(summary = "获取用户列表", description = "获取所有用户信息")
    public Result<List<AccountVO>> getAccountsAsList() {
        return Result.dataMessageHandler(() -> accountService.listAccounts(), "用户列表为空");
    }

    @PostMapping("/save_user")
    @Operation(summary = "保存用户", description = "创建新的用户")
    public Result<Void> saveUser(@RequestBody @Valid @Parameter(description = "用户详情") AccountDTO dto) {
        return Result.messageHandler(() -> accountService.saveAccount(dto));
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "删除用户", description = "根据ID删除用户")
    public Result<Void> deleteUser(@PathVariable @Parameter(description = "用户ID") Integer id) {
        return Result.messageHandler(() -> accountService.deleteAccountById(id));
    }

    @GetMapping("/get_user/{id}")
    @Operation(summary = "获取用户详情", description = "根据ID获取用户详情")
    public Result<AccountVO> getUser(@PathVariable @Parameter(description = "用户ID") Integer id) {
        return Result.dataMessageHandler(() -> accountService.getAccountById(id), "用户不存在");
    }

    @GetMapping("/update")
    @Operation(summary = "更新用户", description = "更新用户信息")
    public Result<Void> updateUser(@RequestBody @Valid @Parameter(description = "更新后的用户详情") AccountDTO dto) {
        return Result.messageHandler(() -> accountService.updateAccount(dto));
    }
}