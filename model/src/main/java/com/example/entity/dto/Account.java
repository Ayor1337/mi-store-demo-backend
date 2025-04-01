package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("Users")
public class Account implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer userId;       // 用户ID
    private String username;      // 用户名
    private String password;      // 密码（存储时加密，返回时可不包含）
    private String email;         // 邮箱
    private String phone;         // 电话
    private String address;       // 默认收货地址
    private Date createTime;      // 注册时间
}
