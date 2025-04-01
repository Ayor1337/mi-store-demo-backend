package com.example.config;

import com.example.entity.dto.Account;
import com.example.entity.dto.Admin;
import com.example.entity.vo.response.AuthorizeVO;
import com.example.result.Result;
import com.example.result.ResultCodeEnum;
import com.example.service.AdminService;
import com.example.filter.JWTAuthorizeFilter;
import com.example.util.JWTUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
public class SecurityConfiguration {

    @Resource
    JWTAuthorizeFilter jwtFilter;

    @Resource
    AdminService adminService;

    @Resource
    JWTUtil jwtUtil;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/auth/**").permitAll();
                    auth.anyRequest().authenticated();
                })
                .formLogin(conf -> {
                    conf.loginProcessingUrl("/api/auth/login");
                    conf.successHandler(this::onAuthenticationSuccess);
                    conf.failureHandler(this::onAuthenticationFailure);
                })
                .logout(conf -> {
                    conf.logoutUrl("/api/auth/logout");
                })
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(conf -> {
                    conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    public void onAuthenticationSuccess(HttpServletRequest req,
                                        HttpServletResponse resp,
                                        Authentication auth) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        User user = (User) auth.getPrincipal();
        Admin admin = adminService.getOneByUsername(user.getUsername());
        String token = jwtUtil.createJwt(user, admin.getAdminId(), admin.getUsername());
        AuthorizeVO vo = new AuthorizeVO();
        BeanUtils.copyProperties(admin, vo);
        vo.setToken(token);
        vo.setExpire(jwtUtil.expiredTime());
        resp.getWriter().write(Result.ok(vo).toJSONString());
    }

    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException exception) throws IOException, ServletException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(Result.fail(401, exception.getMessage()).toJSONString());
    }
}
