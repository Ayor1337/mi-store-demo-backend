package com.example.entity.vo.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReqProductVO {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Integer categoryId;
}
