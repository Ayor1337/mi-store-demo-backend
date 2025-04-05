package com.example.config;

import com.example.entity.pojo.Admin;
import com.example.entity.vo.AuthorizeVO;
import com.example.result.Result;
import com.example.result.ResultCodeEnum;
import com.example.service.AdminService;
import com.example.filter.JWTAuthorizeFilter;
import com.example.util.JWTUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;

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
                    conf.logoutSuccessHandler(this::onLogoutSuccess);
                })
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(conf -> {
                    conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(conf -> {
                    conf.authenticationEntryPoint(this::onUnauthorized);
                    conf.accessDeniedHandler(this::onAccessDeny);
                })
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

    public void onLogoutSuccess(HttpServletRequest req,
                                HttpServletResponse resp,
                                Authentication auth) throws IOException, ServletException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        String authorization = req.getHeader("Authorization");
        PrintWriter writer = resp.getWriter();
        // 校验是否登录，如果没有登录就不可能退出登录
        if (jwtUtil.invalidateJWT(authorization)) {
            writer.write(Result.build(null, ResultCodeEnum.LOGOUT_SUCCESS).toJSONString());
        } else {
            writer.write(Result.build(null, ResultCodeEnum.LOGOUT_FAILURE).toJSONString());
        }
    }


    // 未验证异常
    public void onUnauthorized(HttpServletRequest req,
                               HttpServletResponse resp,
                               AuthenticationException e) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        resp.getWriter().write(Result.build(null, ResultCodeEnum.UNAUTHENTICATED_ERROR).toJSONString());
    }

    // 访问权限异常 Handler
    public void onAccessDeny(HttpServletRequest req,
                             HttpServletResponse resp,
                             AccessDeniedException e) throws IOException, ServletException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        resp.getWriter().write(Result.build(null, ResultCodeEnum.ADMIN_ACCESS_FORBIDDEN).toJSONString());
    }
}
