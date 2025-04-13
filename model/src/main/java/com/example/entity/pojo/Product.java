package com.example.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("Products")
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Product", description = "商品信息")
public class Product implements Serializable {

    @TableId(type = IdType.AUTO)
    @Schema(description = "商品ID")
    private Integer productId;    // 商品ID

    @Length(max = 20)
    @Schema(description = "商品名称")
    private String name;          // 商品名称

    @Schema(description = "商品描述")
    private String description;   // 商品描述

    @Schema(description = "所属分类ID")
    private Integer categoryId;   // 所属分类ID

    @Schema(description = "商品图片地址")
    private String imageUrl;      // 商品图片地址

    @Schema(description = "添加时间")
    private Date createTime;      // 添加时间
}
