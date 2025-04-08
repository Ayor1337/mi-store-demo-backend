package com.example.service.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.AccountDTO;
import com.example.entity.pojo.Account;
import com.example.entity.vo.AccountVO;
import com.example.mapper.AccountMapper;
import com.example.service.AccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    private final BCryptPasswordEncoder passwordEncoder;

    public AccountServiceImpl(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<AccountVO> listAccount() {
        List<Account> list = this.list();
        List<AccountVO> voList = new ArrayList<>();
        if (list == null)
            return null;
        list.forEach(account -> {
            AccountVO accountVO = new AccountVO();
            BeanUtils.copyProperties(account, accountVO);
            voList.add(accountVO);
        });
        return voList;
    }

    @Override
    public AccountVO getAccountById(Integer id) {
        Account account = this.getById(id);
        if (account == null) {
            return null;
        }
        AccountVO accountVO = new AccountVO();
        BeanUtils.copyProperties(account, accountVO);
        return accountVO;
    }

    @Override
    @Transactional
    public String saveAccount(AccountDTO dto) {
        if (dto == null || existsAccountById(dto.getUserId())) {
            return "用户已存在或数据为空";
        }
        Account account = new Account();
        account.setUsername(dto.getUsername());
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        account.setEmail(dto.getEmail());
        account.setPhone(dto.getPhone());
        account.setAddress(dto.getAddress());
        this.save(account);
        return null;
    }

    @Override
    @Transactional
    public String deleteAccountById(Integer id) {
        if (id == null || !existsAccountById(id)) {
            return "用户不存在或参数为空";
        }
        this.removeById(id);
        return null;
    }

    @Override
    @Transactional
    public String updateAccount(AccountDTO dto) {
        if (dto == null || !existsAccountById(dto.getUserId())) {
            return "";
        }
        Account account = new Account();
        BeanUtils.copyProperties(dto, account);
        this.updateById(account);
        return null;
    }

    private boolean existsAccountById(Integer userId) {
        return this.baseMapper.exists(Wrappers.<Account>lambdaQuery().eq(Account::getUserId, userId));
    }
}
