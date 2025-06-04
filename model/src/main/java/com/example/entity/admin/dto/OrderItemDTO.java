package com.example.entity.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "订单详情信息上传对象")
public class OrderItemDTO {

    @Schema(description = "订单详情ID")
    private Integer orderItemId;

    @Schema(description = "订单ID")
    private Integer orderId;

    @Schema(description = "商品ID")
    private Integer commodityId;

    @Schema(description = "购买数量")
    private Integer quantity;

}
