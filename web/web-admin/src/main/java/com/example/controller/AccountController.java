package com.example.controller;

import com.example.entity.dto.AccountDTO;
import com.example.entity.pojo.Account;
import com.example.result.Result;
import com.example.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/list")
    public Result<List<Account>> getAccountsAsList() {
        return Result.ok(accountService.getAccounts());
    }

    @GetMapping("/save_user")
    public Result<Void> saveUser(AccountDTO dto) {
        return Result.ok();
    }

    @GetMapping("/delete/{id}")
    public Result<Void> deleteUser(@PathVariable Integer id) {
        return Result.ok();
    }

    @GetMapping("/get_user/{id}")
    public Result<Account> getUser(@PathVariable Integer id) {
        return Result.ok();
    }

    @GetMapping("/update")
    public Result<Void> updateUser(AccountDTO dto) {
        return Result.ok();
    }
}
