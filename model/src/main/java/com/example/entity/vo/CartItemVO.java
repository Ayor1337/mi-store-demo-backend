package com.example.entity.vo;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemVO {
    private Integer cartItemId;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalPrice;
}
