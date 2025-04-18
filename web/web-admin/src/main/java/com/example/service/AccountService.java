package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.AccountDTO;
import com.example.entity.pojo.Account;
import com.example.entity.vo.AccountVO;

import java.util.List;

public interface AccountService extends IService<Account> {

    List<AccountVO> listAccounts();

    AccountVO getAccountById(Integer id);

    String saveAccount(AccountDTO dto);

    String deleteAccountById(Integer id);

    String updateAccount(AccountDTO dto);
}
