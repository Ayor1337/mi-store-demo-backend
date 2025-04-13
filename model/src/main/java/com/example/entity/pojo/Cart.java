package com.example.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("Cart")
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Cart", description = "购物车信息")
public class Cart implements Serializable {

    @TableId(type = IdType.AUTO)
    @Schema(description = "购物车ID")
    private Integer cartId;       // 购物车ID

    @Schema(description = "用户ID")
    private Integer userId;       // 用户ID

    @Schema(description = "购物车创建时间")
    private Date createTime;      // 购物车创建时间
}
