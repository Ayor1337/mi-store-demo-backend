package com.example.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "商品全名视图对象")
public class CommodityWithFullNameVO {
    @Schema(description = "商品ID")
    private Integer commodityId;

    @Schema(description = "商品全名")
    private String fullName;
}