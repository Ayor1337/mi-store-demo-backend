package com.example.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Schema(name = "CommodityDTO", description = "商品上传对象")
public class CommodityDTO {

    @Schema(description = "商品ID")
    private Integer commodityId;

    @Schema(description = "商品所属产品ID")
    private Integer productId;

    @Schema(description = "商品SKU")
    private String sku;

    @Schema(description = "商品描述")
    private String description;

    @Schema(description = "商品库存")
    @Min(value = 0, message = "库存必须大于或等于0")
    private Integer stock;

    @Schema(description = "商品价格")
    @Min(value = 0, message = "价格必须大于或等于0")
    private BigDecimal price;

    @Schema(description = "商品规格")
    private Map<String, Object> specifications;

    @Schema(description = "商品图片")
    private Map<String, String> images;
}
