package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Admin;
import com.example.mapper.AdminMapper;
import com.example.service.AdminService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = this.getOneByUsername(username);
        if (admin == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return User
                .withUsername(username)
                .password(admin.getPassword())
                .roles("admin")
                .build();
    }


    @Override
    public Admin getOneByUsername(String username) {

        return this.lambdaQuery()
                .eq(Admin::getUsername, username)
                .one();

    }
}
