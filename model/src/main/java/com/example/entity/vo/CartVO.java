package com.example.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class CartVO {
    private Integer cartId;
    private List<CartItemVO> cartItems;
}
