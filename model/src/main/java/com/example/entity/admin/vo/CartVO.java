package com.example.entity.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "CartVO", description = "购物车视图对象")
public class CartVO {
    @Schema(description = "购物车ID")
    private Integer cartId;

    @Schema(description = "购物车详情")
    private List<CartItemVO> cartItems;
}
