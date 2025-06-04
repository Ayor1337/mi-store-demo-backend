package com.example.entity.app.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVO {
    private Integer productId;

    private String name;

    private String description;

    private String imageUrl;

    private Integer categoryId;

    private BigDecimal price;
}
