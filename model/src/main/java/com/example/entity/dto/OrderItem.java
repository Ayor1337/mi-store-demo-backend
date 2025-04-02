package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("OrderItems")
public class OrderItem implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer orderItemId;   // 订单详情ID
    private Integer orderId;       // 订单ID
    private Integer productId;     // 商品ID
    private Integer quantity;      // 购买数量
    private BigDecimal price;      // 购买时的单价
}