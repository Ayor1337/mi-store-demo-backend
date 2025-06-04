package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.admin.dto.AccountDTO;
import com.example.entity.admin.dto.Base64Upload;
import com.example.entity.admin.vo.AccountVO;
import com.example.entity.pojo.Account;

import java.util.List;

public interface AccountService extends IService<Account> {

    List<AccountVO> getAllAccountVOs();

    AccountVO getAccountById(Integer id);

    String saveAccount(AccountDTO dto);

    String deleteAccountById(Integer id);

    String updateAccount(AccountDTO dto);

    String getAvatarUrlById(Integer userId);

    String saveAvatar(Integer userId, Base64Upload file);

    String updateAvatarUrl(Integer userId, String fileUrl);
}
