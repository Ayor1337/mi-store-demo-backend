package com.example.entity.app.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Schema(description = "商品视图对象")
public class CommodityVO {
    @Schema(description = "商品ID")
    private Integer commodityId;

    @Schema(description = "产品ID")
    private Integer productId;

    @Schema(description = "SKU")
    private String sku;

    @Schema(description = "商品描述")
    private String description;

    @Schema(description = "库存数量")
    private Integer stock;

    @Schema(description = "价格")
    private BigDecimal price;

    @Schema(description = "规格")
    private Map<String, Object> specifications;

    @Schema(description = "商品图片数组")
    private String[] images;

    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        sb.append(sku)
                .append(" ");
        specifications.forEach((k, v) -> {
            sb.append(v)
                    .append(" ");
        });
        return sb.toString();
    }
}