package com.example.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("Admins")
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Admin", description = "管理员信息")
public class Admin {

    @TableId(type = IdType.AUTO)
    @Schema(description = "管理员ID")
    private Integer adminId;

    @Schema(description = "管理员名")
    private String username;

    @Schema(description = "管理员密码")
    private String password;
}
