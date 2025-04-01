package com.example.config;

import com.example.filter.JWTAuthorizeFilter;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//@Configuration
public class SecurityConfiguration {


    @Resource
    JWTAuthorizeFilter jwtFilter;

    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> {
                })
                .formLogin(conf -> {
                    conf.loginProcessingUrl("/api/auth/login");
                })
                .logout(conf -> {
                    conf.logoutUrl("/api/auth/logout");
                })
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
