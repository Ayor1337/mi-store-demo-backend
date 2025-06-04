package com.example.entity.mapperResult;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CommodityInOrderItem {
    private Integer commodityId;
    private Integer quantity;
    private BigDecimal price;
}
