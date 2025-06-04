package com.example.entity.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "订单详情视图对象")
public class OrderItemVO {

    @Schema(description = "订单详情ID")
    private Integer orderItemId;

    @Schema(description = "订单ID")
    private Integer orderId;

    @Schema(description = "商品ID")
    private Integer commodityId;

    @Schema(description = "商品名称")
    private String commodityName;

    @Schema(description = "购买数量")
    private Integer quantity;

    @Schema(description = "购买时的单价")
    private BigDecimal price;

}
