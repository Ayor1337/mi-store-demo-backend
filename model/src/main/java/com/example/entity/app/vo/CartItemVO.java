package com.example.entity.app.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemVO {
    private Integer cartItemId;

    private String sku;

    private String description;

    private BigDecimal price;

    private Integer quantity;

    private BigDecimal totalPrice;

    private String imageUrl;

    private Boolean isChecked;
}
