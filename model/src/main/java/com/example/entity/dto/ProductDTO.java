package com.example.entity.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    private Integer productId;
    @Length(max=20)
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Integer categoryId;
}
