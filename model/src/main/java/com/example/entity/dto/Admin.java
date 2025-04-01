package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("Admins")
public class Admin {
    @TableId(type = IdType.AUTO)
    private Integer adminId;
    private String username;
    private String password;
}
