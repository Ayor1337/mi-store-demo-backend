package com.example.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "产品视图对象")
public class ProductVO {
    @Schema(description = "产品ID")
    private Integer productId;

    @Schema(description = "产品名称")
    private String name;

    @Schema(description = "产品描述")
    private String description;

    @Schema(description = "产品图片URL")
    private String imageUrl;

    @Schema(description = "产品类别ID")
    private Integer categoryId;
}