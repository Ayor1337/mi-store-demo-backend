package com.example.entity.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@TableName("CartItems")
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "CartItem", description = "购物车详情信息")
public class CartItem implements Serializable {

    @TableId(type = IdType.AUTO)
    @Schema(description = "购物车详情ID")
    private Integer cartItemId;   // 购q物车详情ID

    @Schema(description = "购物车ID")
    private Integer cartId;       // 购物车ID

    @Schema(description = "商品ID")
    private Integer commodityId;    // 商品ID

    @Schema(description = "添加数量")
    private Integer quantity;     // 添加数量
}
