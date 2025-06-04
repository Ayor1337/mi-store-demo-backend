package com.example.entity.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "订单视图对象")
public class OrderVO {
    @Schema(description = "订单ID")
    private Integer orderId;

    @Schema(description = "用户ID")
    private Integer userId;

    @Schema(description = "总价格")
    private BigDecimal totalPrice;

    @Schema(description = "订单状态")
    private String status;

    @Schema(description = "支付状态")
    private String paymentStatus;

    @Schema(description = "配送地址")
    private String address;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;
}