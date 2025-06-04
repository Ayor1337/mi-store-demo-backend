package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.admin.dto.Base64Upload;
import com.example.entity.app.dto.CodeAuth;
import com.example.entity.app.dto.PasswordResetDTO;
import com.example.entity.app.dto.UserRegisterDTO;
import com.example.entity.app.vo.UserinfoVO;
import com.example.entity.pojo.Account;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends IService<Account>, UserDetailsService {

    Account getOneByUsernameOrEmailOrPhone(String username);

    String getNicknameByAuthentication(String token);

    String register(UserRegisterDTO dto);

    boolean existUserById(Integer id);

    Integer getUserIdByUsername(String username);

    UserinfoVO getUserinfoByUserId(Integer userId);

    String updateUsernameByUserId(Integer userId, String username);

    String saveAvatar(Integer userId, Base64Upload file);

    String updateAvatarUrl(Integer userId, String fileUrl);

    String resetEmailVerifyCode(String email, String ip);

    String authorizeEmailCode(CodeAuth vo);

    String resetPassword(PasswordResetDTO dto);
}
