package com.example.controller;

import com.example.entity.dto.AccountDTO;
import com.example.entity.vo.AccountVO;
import com.example.result.Result;
import com.example.result.ResultCodeEnum;
import com.example.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/list")
    public Result<List<AccountVO>> getAccountsAsList() {
        if (accountService == null) {
            return Result.build(null, ResultCodeEnum.DATA_NOT_FOUND);
        }
        return Result.ok(accountService.listAccount());
    }

    @PostMapping("/save_user")
    public Result<Void> saveUser(@RequestBody AccountDTO dto) {
        return Result.messageHandler(() -> accountService.saveAccount(dto));
    }

    @PostMapping("/delete/{id}")
    public Result<Void> deleteUser(@PathVariable Integer id) {
        return Result.messageHandler(() -> accountService.deleteAccountById(id));
    }

    @GetMapping("/get_user/{id}")
    public Result<AccountVO> getUser(@PathVariable Integer id) {
        AccountVO vo = accountService.getAccountById(id);
        if (vo == null)
            return Result.fail(400, "用户不存在");
        return Result.ok(vo);

    }

    @GetMapping("/update")
    public Result<Void> updateUser(AccountDTO dto) {
        return Result.messageHandler(() -> accountService.updateAccount(dto));
    }
}
