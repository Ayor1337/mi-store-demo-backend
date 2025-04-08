package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.pojo.Category;
import com.example.entity.vo.CategoryTreeVO;
import com.example.entity.vo.CategoryVO;

import java.util.List;

public interface CategoryService extends IService<Category> {
    List<CategoryVO> listCategory();

    List<CategoryTreeVO> getCategoryTree();
}
