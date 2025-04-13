package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.CategoryDTO;
import com.example.entity.pojo.Category;
import com.example.entity.vo.CategoryVO;

import java.util.List;

public interface CategoryService extends IService<Category> {
    List<CategoryVO> listCategory();

    String getCategoryNameById(Integer categoryId);

    String saveCategory(CategoryDTO dto);

    CategoryVO getCategoryById(Integer categoryId);

    String removeCategoryById(Integer categoryId);
}
