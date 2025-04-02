package com.example.entity.dto;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@TableName("CartItems")
@AllArgsConstructor
@NoArgsConstructor
public class CartItem implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer cartItemId;   // 购物车详情ID
    private Integer cartId;       // 购物车ID
    private Integer productId;    // 商品ID
    private Integer quantity;     // 添加数量
}
