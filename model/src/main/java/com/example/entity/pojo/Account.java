package com.example.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("Users")
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "User", description = "用户信息")
public class Account implements Serializable {

    @TableId(type = IdType.AUTO)
    @Schema(description = "用户ID")
    private Integer userId;       // 用户ID

    @Schema(description = "用户名")
    private String username;      // 用户名

    @Schema(description = "昵称")
    private String nickname;

    @JsonIgnore
    @Schema(description = "密码")
    private String password;      // 密码（存储时加密，返回时可不包含）

    @Schema(description = "邮箱")
    private String email;         // 邮箱

    @Schema(description = "电话")
    private String phone;         // 电话

    @Schema(description = "地址")
    private String address;       // 默认收货地址

    @Schema(description = "头像")
    private String avatarUrl;

    @Schema(description = "注册时间", example = "2021-01-01 00:00:00")
    private Date createTime;      // 注册时间
}
