package com.example.entity.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "CategoryVO", description = "商品分类视图对象")
public class CategoryVO {

    @Schema(description = "分类ID")
    private Integer categoryId;    // 分类ID

    @Schema(description = "分类名称")
    private String name;           // 分类名称

    @Schema(description = "分类描述")
    private String description;    // 分类描述
}
