package com.example.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.admin.dto.Base64Upload;
import com.example.entity.app.dto.CodeAuth;
import com.example.entity.app.dto.PasswordResetDTO;
import com.example.entity.app.dto.UserRegisterDTO;
import com.example.entity.app.vo.UserinfoVO;
import com.example.entity.pojo.Account;
import com.example.mapper.AccountMapper;
import com.example.minio.MinioService;
import com.example.service.AccountService;
import com.example.service.CartService;
import com.example.util.CONST;
import com.example.util.FlowUtils;
import com.example.util.JWTUtil;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.example.util.DataEncoder.getAnonymousEmail;
import static com.example.util.DataEncoder.getAnonymousPhone;

@Component
@Transactional
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Resource
    private JWTUtil jwtUtil;

    @Resource
    private PasswordEncoder encoder;

    @Resource
    private CartService cartService;

    @Resource
    private MinioService minioService;

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private FlowUtils flowUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = this.getOneByUsernameOrEmailOrPhone(username);
        if (account == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return User
                .withUsername(username)
                .password(account.getPassword())
                .roles("user")
                .build();
    }


    @Override
    public Account getOneByUsernameOrEmailOrPhone(String username) {
        return this.lambdaQuery()
                .eq(Account::getUsername, username)
                .or()
                .eq(Account::getEmail, username)
                .or()
                .eq(Account::getPhone, username)
                .one();
    }

    @Override
    public String getNicknameByAuthentication(String token) {
        DecodedJWT decodedJWT = jwtUtil.resolveJwt(token);
        if (decodedJWT == null)
            return null;
        String idString = decodedJWT.getClaim("id").toString();
        int parseInt = Integer.parseInt(idString);
        Account account = this.getById(parseInt);
        if (account != null)
            return account.getNickname();
        return null;
    }

    @Override
    public String register(UserRegisterDTO dto) {
        String username = dto.getUsername();
        if (username == null || username.isEmpty()) {
            return "用户名不能为空";
        }
        if (this.getOneByUsernameOrEmailOrPhone(username) != null) {
            return "用户名已被注册";
        }
        String password = dto.getPassword();
        if (password == null || password.isEmpty()) {
            return "密码不能为空";
        }
        if (password.length() < 8) {
            return "密码长度不能小于8位";
        }
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(encoder.encode(password));
        account.setEmail(dto.getEmail());
        account.setPhone(dto.getPhone());
        account.setNickname(username + UUID.randomUUID().toString().split("-")[0]);

        this.save(account);
        cartService.createCart(account.getUserId());
        return null;
    }

    @Override
    public boolean existUserById(Integer id) {
        return this.baseMapper.exists(Wrappers.<Account>lambdaQuery().eq(Account::getUserId, id));
    }

    @Override
    public Integer getUserIdByUsername(String username) {
        return this.lambdaQuery().eq(Account::getUsername, username).one().getUserId();
    }

    @Override
    public UserinfoVO getUserinfoByUserId(Integer userId) {
        Account account = this.getById(userId);
        if (account == null) {
            return null;
        }

        UserinfoVO userinfoVO = new UserinfoVO();
        userinfoVO.setUserId(account.getUserId());
        userinfoVO.setAvatarUrl(account.getAvatarUrl());
        userinfoVO.setPhone(getAnonymousPhone(account.getPhone()));
        userinfoVO.setEmail(getAnonymousEmail(account.getEmail()));
        userinfoVO.setUsername(account.getNickname());

        return userinfoVO;
    }


    @Override
    public String updateUsernameByUserId(Integer userId, String username) {
        Account account = this.getById(userId);
        account.setNickname(username);
        return this.updateById(account) ? null : "更新失败";
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

    @Override
    public String resetEmailVerifyCode(String email, String ip) {
        synchronized (ip.intern()) {
            if (!this.verifyLimit(ip)) {
                return "请求频繁，请稍后再试";
            }
            if (!existAccountByEmail(email)) {
                return "该邮箱账户不存在";
            }
            return getCodeAndSave(email);
        }
    }

    @Override
    public String authorizeEmailCode(CodeAuth vo) {
        String email = vo.getEmail();
        String key = CONST.VERIFY_EMAIL_DATA + email;
        String code = redisTemplate.opsForValue().get(key);

        if (code == null) {
            return "请先获取验证码";
        }
        if (!code.equals(vo.getCode())) {
            return "验证码错误";
        }
        return null;
    }

    @Override
    public String resetPassword(PasswordResetDTO dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();
        String key = CONST.VERIFY_EMAIL_DATA + email;
        String code = redisTemplate.opsForValue().get(key);


        if (code == null || !code.equals(dto.getCode())) {
            return "状态已失效，请重新获取验证码并重置";
        }
        String newPassword = encoder.encode(password);

        boolean updatedRows = this.lambdaUpdate()
                .eq(Account::getEmail, email)
                .set(Account::getPassword, newPassword)
                .update();

        return updatedRows ? null : "邮箱不存在，重置失败";
    }

    private boolean existAccountByEmail(String email) {
        return this.baseMapper.exists(Wrappers.<Account>lambdaQuery().eq(Account::getEmail, email));
    }

    private String getCodeAndSave(String email) {
        int code = new Random().nextInt(899999) + 100000;
        Map<String, Object> data = Map.of("email", email, "code", code);

        rabbitTemplate.convertAndSend("email", "send_email", data);
        redisTemplate.opsForValue()
                .set(CONST.VERIFY_EMAIL_DATA + email, String.valueOf(code), 3, TimeUnit.MINUTES);

        return null;
    }

    private boolean verifyLimit(String ip) {
        String key = CONST.VERIFY_EMAIL_LIMIT + ip;
        return flowUtils.limitOnceCheck(key, 60);
    }


}
