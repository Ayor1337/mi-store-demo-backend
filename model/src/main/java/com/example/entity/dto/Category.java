package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("Category")
public class Category implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer categoryId;   // 分类ID
    private String name;          // 分类名称
    private String description;   // 分类描述
    private Integer parentId;     // 父级分类ID（顶级分类为null或0）
}
