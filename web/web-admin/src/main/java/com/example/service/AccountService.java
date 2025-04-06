package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.pojo.Account;
import com.example.result.Result;

import java.util.List;

public interface AccountService extends IService<Account> {

    List<Account> getAccounts();
}
