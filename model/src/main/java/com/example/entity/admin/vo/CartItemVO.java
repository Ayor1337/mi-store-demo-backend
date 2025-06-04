package com.example.entity.admin.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(name = "CartItemVO", description = "购物车详情视图对象")
public class CartItemVO {
    @Schema(description = "购物车详情ID")
    private Integer cartItemId;

    @Schema(description = "商品SKU")
    private String sku;

    @Schema(description = "商品描述")
    private String description;

    @Schema(description = "商品库存")
    private Integer stock;

    @Schema(description = "是否勾选")
    private Boolean isChecked;

    @Schema(description = "商品价格")
    private BigDecimal price;

    @Schema(description = "购买数量")
    private Integer quantity;

    @Schema(description = "商品总价")
    private BigDecimal totalPrice;
}
