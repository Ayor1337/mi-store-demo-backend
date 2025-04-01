package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("Cart")
public class Cart implements Serializable {
    private Integer cartId;       // 购物车ID
    private Integer userId;       // 用户ID
    private Date createTime;      // 购物车创建时间
}
