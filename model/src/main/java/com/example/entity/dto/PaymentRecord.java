package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("PaymentRecords")
public class PaymentRecord implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer paymentId;       // 支付记录ID
    private Integer orderId;         // 订单ID
    private String paymentMethod;    // 支付方式（如支付宝、微信、信用卡等）
    private BigDecimal amount;       // 支付金额
    private Date paymentTime;        // 支付时间
    private String transactionId;    // 第三方支付交易号
}
