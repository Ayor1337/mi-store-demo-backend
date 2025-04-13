package com.example.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@TableName("Categories")
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Category", description = "商品分类信息")
public class Category implements Serializable {

    @TableId(type = IdType.AUTO)
    @Schema(description = "分类ID")
    private Integer categoryId;   // 分类ID

    @Schema(description = "分类名称")
    private String name;          // 分类名称

    @Schema(description = "分类描述")
    private String description;   // 分类描述
}
