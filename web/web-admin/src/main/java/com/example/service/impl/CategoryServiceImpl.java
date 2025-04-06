package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.pojo.Category;
import com.example.entity.vo.CategoryVO;
import com.example.mapper.CategoryMapper;
import com.example.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public List<CategoryVO> getCategoryTree() {
        // 1. 查询所有分类数据
        List<Category> categoryEntities = list();

        // 2. 将实体转换为 VO 对象
        List<CategoryVO> voList = categoryEntities.stream().map(entity -> {
            CategoryVO vo = new CategoryVO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        }).collect(Collectors.toList());

        // 3. 构建树形结构并返回顶级分类列表
        return buildCategoryTree(voList);
    }

    /**
     * 根据平铺的分类 VO 数据构建树形结构
     */
    private List<CategoryVO> buildCategoryTree(List<CategoryVO> voList) {
        List<CategoryVO> tree = new ArrayList<>();
        // 筛选出顶级分类，通常 parentId 为 null 或 0 的为顶级分类
        for (CategoryVO vo : voList) {
            if (vo.getParentId() == null || vo.getParentId() == 0) {
                tree.add(vo);
            }
        }
        // 为每个顶级分类递归查找并设置子分类
        for (CategoryVO parent : tree) {
            parent.setChildren(findChildren(parent, voList));
        }
        return tree;
    }

    /**
     * 递归查找当前分类的所有子分类
     */
    private List<CategoryVO> findChildren(CategoryVO parent, List<CategoryVO> voList) {
        List<CategoryVO> children = new ArrayList<>();
        for (CategoryVO vo : voList) {
            if (parent.getCategoryId().equals(vo.getParentId())) {
                children.add(vo);
            }
        }
        // 对每个子分类递归设置其下级子分类
        for (CategoryVO child : children) {
            child.setChildren(findChildren(child, voList));
        }
        return children;
    }
}
