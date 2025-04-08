package com.example.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryTreeVO {
    private Integer categoryId;    // 分类ID
    private String name;           // 分类名称
    private String description;    // 分类描述
    private Integer parentId;      // 父级分类ID，顶级分类可为 null 或 0

    // 存放子分类数据，用于构建树形结构
    private List<CategoryTreeVO> children = new ArrayList<>();
}
