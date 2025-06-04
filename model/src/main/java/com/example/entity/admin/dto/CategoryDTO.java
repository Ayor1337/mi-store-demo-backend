package com.example.entity.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "CategoryDTO", description = "分类信息上传对象")
public class CategoryDTO {

    @Schema(description = "分类ID")
    private Integer categoryId;

    @Schema(description = "分类名称")
    private String name;

    @Schema(description = "分类描述")
    private String description;
}
