package com.example.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "OrderDTO", description = "订单信息上传对象")
public class OrderDTO {

    @Schema(description = "订单ID")
    private Integer orderId;

    @Schema(description = "用户ID")
    private Integer userId;

    @Schema(description = "订单状态")
    private String status;

    @Schema(description = "支付状态")
    private String paymentStatus;

    @Schema(description = "收货地址")
    private String shippingAddress;
}
