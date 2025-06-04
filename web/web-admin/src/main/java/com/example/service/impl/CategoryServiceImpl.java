package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.admin.dto.CategoryDTO;
import com.example.entity.admin.vo.CategoryVO;
import com.example.entity.pojo.Category;
import com.example.mapper.CategoryMapper;
import com.example.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    // 查询所有分类数据
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

    // 根据分类ID获取分类名称
    @Override
    public String getCategoryNameById(Integer categoryId) {
        Category category = getById(categoryId);
        if (category != null) {
            return category.getName();
        }
        return null;
    }

    // 保存分类信息
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

    // 根据分类ID获取分类信息
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

    // 根据分类ID删除分类信息
    @Override
    public String deleteCategoryById(Integer categoryId) {
        //TODO 不能删除已经有商品的分类
        if (categoryId == null) {
            return "id为空";
        }
        this.removeById(categoryId);
        return null;
    }

    // 根据关键字搜索分类名称并返回匹配的分类ID列表
    @Override
    public List<Integer> searchCategoryIds(String keyword) {
        return this.lambdaQuery()
                .like(Category::getName, keyword)
                .list()
                .stream()
                .map(Category::getCategoryId)
                .toList();
    }
}
