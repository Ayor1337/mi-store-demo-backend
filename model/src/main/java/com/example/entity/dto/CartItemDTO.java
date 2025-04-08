package com.example.entity.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    private Integer cartItemId;
    private Integer cartId;
    private Integer productId;
    private Integer quantity;
}
