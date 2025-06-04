package com.example.entity.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(name = "ProductDTO", description = "产品信息上传对象")
public class ProductDTO {

    @Schema(description = "产品ID")
    private Integer productId;

    @Length(max = 100)
    @Schema(description = "产品名称")
    private String name;

    @Schema(description = "产品描述")
    private String description;

    @Schema(description = "产品所属分类ID")
    private Integer categoryId;

    @Schema(description = "产品图片")
    private String base64;

    @Schema(description = "产品图片文件名")
    private String fileName;
}
