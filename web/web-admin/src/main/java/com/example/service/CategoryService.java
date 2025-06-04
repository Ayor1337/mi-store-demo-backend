package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.admin.dto.CategoryDTO;
import com.example.entity.admin.vo.CategoryVO;
import com.example.entity.pojo.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {
    List<CategoryVO> listCategory();

    String getCategoryNameById(Integer categoryId);

    String saveCategory(CategoryDTO dto);

    CategoryVO getCategoryById(Integer categoryId);

    String deleteCategoryById(Integer categoryId);
}
