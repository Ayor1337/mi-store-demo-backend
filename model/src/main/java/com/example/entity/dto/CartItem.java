package com.example.entity.dto;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("CartItem")
public class CartItem implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer cartItemId;   // 购物车详情ID
    private Integer cartId;       // 购物车ID
    private Integer productId;    // 商品ID
    private Integer quantity;     // 添加数量
}
