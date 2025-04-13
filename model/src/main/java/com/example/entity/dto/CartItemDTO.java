package com.example.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Schema(name = "CartItemDTO", description = "购物车内项目上传对象")
public class CartItemDTO {
    @Schema(description = "购物车项ID")
    private Integer cartItemId;

    @Schema(description = "购物车ID")
    private Integer cartId;

    @Schema(description = "商品ID")
    private Integer commodityId;


    @Min(value = 0, message = "数量必须大于或等于0")
    @Schema(description = "数量")
    private Integer quantity;
}
