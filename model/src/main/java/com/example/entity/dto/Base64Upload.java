package com.example.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Base64Upload", description = "base64上传对象")
public class Base64Upload {
    @Schema(description = "base64编码")
    private String base64;

    @Schema(description = "文件名")
    private String fileName;

}
