package com.example.entity.app.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemVO {
    private Integer orderId;

    private Integer orderItemId;

    private String name;

    private String imageUrl;

    private BigDecimal price;

    private Integer quantity;
}
