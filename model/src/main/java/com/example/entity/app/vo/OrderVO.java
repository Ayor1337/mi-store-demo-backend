package com.example.entity.app.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderVO {

    private Integer orderId;

    private String status;

    private Date createTime;

    private String paymentStatus;

    private String name;

    private BigDecimal totalPrice;
}
