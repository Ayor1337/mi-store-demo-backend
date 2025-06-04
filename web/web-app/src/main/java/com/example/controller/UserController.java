package com.example.controller;

import com.example.entity.admin.dto.Base64Upload;
import com.example.entity.app.dto.CodeAuth;
import com.example.entity.app.dto.PasswordResetDTO;
import com.example.entity.app.dto.UserRegisterDTO;
import com.example.entity.app.vo.UserinfoVO;
import com.example.result.Result;
import com.example.service.AccountService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private AccountService accountService;

    @GetMapping("/info/username")
    public Result<String> getUsername(HttpServletRequest request) {
        return Result.dataMessageHandler(() -> accountService.getNicknameByAuthentication(request.getHeader("Authorization")), "用户未登录");
    }

    @PostMapping("/auth/register")
    public Result<Void> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        return Result.messageHandler(() -> accountService.register(userRegisterDTO));
    }

    @GetMapping("/userinfo")
    public Result<UserinfoVO> getUserinfo(HttpServletRequest request) {
        Integer userId = Integer.parseInt(request.getAttribute("id").toString());
        return Result.dataMessageHandler(() -> accountService.getUserinfoByUserId(userId), "获取用户信息失败");
    }

    @PostMapping("/update_username")
    public Result<Void> updateUsername(@RequestParam("username") String username, HttpServletRequest request) {
        Integer userId = Integer.parseInt(request.getAttribute("id").toString());
        return Result.messageHandler(() -> accountService.updateUsernameByUserId(userId, username));
    }

    @PostMapping("/save_avatar")
    public Result<Void> uploadUserAvatar(@RequestBody @Parameter(description = "Base64编码的图片") Base64Upload file,
                                         HttpServletRequest request) {
        Integer userId = Integer.parseInt(request.getAttribute("id").toString());
        return Result.messageHandler(() -> accountService.saveAvatar(userId, file));
    }

    @PostMapping("/auth/askcode")
    public Result<Void> askCode(@RequestParam("email") String email,
                                HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        return Result.messageHandler(() -> accountService.resetEmailVerifyCode(email, ip));
    }

    @PostMapping("/auth/verify")
    public Result<Void> verify(@RequestBody CodeAuth codeAuth) {
        return Result.messageHandler(() -> accountService.authorizeEmailCode(codeAuth));
    }

    @PostMapping("/auth/resetPassword")
    public Result<Void> resetPassword(@RequestBody PasswordResetDTO dto) {
        return Result.messageHandler(() -> accountService.resetPassword(dto));
    }
}
