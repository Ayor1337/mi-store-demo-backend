package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.CategoryDTO;
import com.example.entity.pojo.Category;
import com.example.entity.vo.CategoryVO;
import com.example.mapper.CategoryMapper;
import com.example.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public List<CategoryVO> listCategory() {
        // 1. 查询所有分类数据
        List<CategoryVO> categoryVOS = new ArrayList<>();
        this.list().forEach(category -> {
            CategoryVO categoryVO = new CategoryVO();
            BeanUtils.copyProperties(category, categoryVO);
            categoryVOS.add(categoryVO);
        });

        return categoryVOS;

    }

    @Override
    public String getCategoryNameById(Integer categoryId) {
        Category category = getById(categoryId);
        if (category != null) {
            return category.getName();
        }
        return null;
    }

    @Override
    public String saveCategory(CategoryDTO dto) {
        if (dto == null) {
            return "内容为空";
        }
        Category category = new Category();
        BeanUtils.copyProperties(dto, category);
        this.save(category);
        return null;
    }

    @Override
    public CategoryVO getCategoryById(Integer categoryId) {
        if (categoryId == null) {
            return null;
        }
        Category category = this.getById(categoryId);
        CategoryVO categoryVO = new CategoryVO();
        BeanUtils.copyProperties(category, categoryVO);

        return categoryVO;
    }

    @Override
    public String removeCategoryById(Integer categoryId) {
        //TODO 不能删除已经有商品的分类
        if (categoryId == null) {
            return "id为空";
        }
        this.removeById(categoryId);
        return null;
    }
}
