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

    /**
     * 搜索分类名称，返回匹配的分类ID集合
     *
     * @param keyword 查询关键字
     * @return 匹配分类ID列表
     */
    List<Integer> searchCategoryIds(String keyword);
}
