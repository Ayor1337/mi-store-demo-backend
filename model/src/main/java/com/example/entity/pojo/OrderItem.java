package com.example.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("OrderItems")
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "OrderItem", description = "订单详情信息")
public class OrderItem implements Serializable {

    @TableId(type = IdType.AUTO)
    @Schema(description = "订单详情ID")
    private Integer orderItemId;   // 订单详情ID

    @Schema(description = "订单ID")
    private Integer orderId;       // 订单ID

    @Schema(description = "商品ID")
    private Integer productId;     // 商品ID

    @Schema(description = "购买数量")
    private Integer quantity;      // 购买数量

    @Schema(description = "购买时的单价")
    private BigDecimal price;      // 购买时的单价
}