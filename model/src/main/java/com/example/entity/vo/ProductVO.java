package com.example.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

//@Builder
@Data
public class ProductVO {

        private String name;
        private String description;
        private BigDecimal price;
        private Integer stock;
        private Integer categoryId;

}
