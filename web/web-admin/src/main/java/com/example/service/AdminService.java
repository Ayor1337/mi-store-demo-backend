package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.pojo.Admin;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AdminService extends IService<Admin>, UserDetailsService {

    Admin getOneByUsername(String username);
}
