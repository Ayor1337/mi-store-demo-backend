package com.example.service.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.admin.dto.AccountDTO;
import com.example.entity.admin.dto.Base64Upload;
import com.example.entity.admin.vo.AccountVO;
import com.example.entity.pojo.Account;
import com.example.mapper.AccountMapper;
import com.example.minio.MinioService;
import com.example.service.AccountService;
import com.example.service.CartService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    private final BCryptPasswordEncoder passwordEncoder;

    public AccountServiceImpl(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Resource
    private CartService cartService;

    @Resource
    private MinioService minioService;

    // 获取所有用户
    @Override
    public List<AccountVO> getAllAccountVOs() {
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

    // 根据id获取用户
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

    // 保存用户
    @Override
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
        return cartService.createCartByUserId(account.getUserId());
    }

    // 根据id删除用户
    @Override
    public String deleteAccountById(Integer id) {
        if (id == null || !existsAccountById(id)) {
            return "用户不存在或参数为空";
        }
        if (cartService.deleteCartByUserId(id) != null) {
            return "删除用户购物车失败";
        }
        this.removeById(id);
        return null;
    }

    // 更新用户
    @Override
    public String updateAccount(AccountDTO dto) {
        if (dto == null || !existsAccountById(dto.getUserId())) {
            return "";
        }
        Account account = new Account();
        BeanUtils.copyProperties(dto, account);
        this.updateById(account);
        return null;
    }

    @Override
    public String getAvatarUrlById(Integer userId) {
        return this.getById(userId).getAvatarUrl();
    }

    @Override
    public String saveAvatar(Integer userId, Base64Upload file) {
        if (file == null) {
            return "图片为空";
        }
        try {
            String url = minioService.uploadAvatar(file);
            return updateAvatarUrl(userId, url);
        } catch (Exception e) {
            return "内部发生错误";
        }
    }

    @Override
    public String updateAvatarUrl(Integer userId, String fileUrl) {
        if (fileUrl == null) {
            return "图片为空";
        }
        Account account = this.getById(userId);
        if (account == null) {
            return "用户不存在";
        }
        account.setAvatarUrl(fileUrl);
        this.updateById(account);
        return null;
    }

    // 根据ID判断用户是否存在
    private boolean existsAccountById(Integer userId) {
        return this.baseMapper.exists(Wrappers.<Account>lambdaQuery().eq(Account::getUserId, userId));
    }
}
