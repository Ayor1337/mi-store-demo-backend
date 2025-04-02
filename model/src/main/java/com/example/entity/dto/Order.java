package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("Orders")
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer orderId;         // 订单ID
    private Integer userId;          // 用户ID
    private BigDecimal totalPrice;   // 订单总金额
    private String status;           // 订单状态（如待支付、待发货、已发货、已完成、已取消）
    private String paymentStatus;    // 支付状态
    private String shippingAddress;  // 配送地址
    private Date createTime;         // 下单时间
    private Date updateTime;         // 订单更新时间
}
