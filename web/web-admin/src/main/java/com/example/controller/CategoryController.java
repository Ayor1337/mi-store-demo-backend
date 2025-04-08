package com.example.controller;

import com.example.entity.vo.CategoryTreeVO;
import com.example.entity.vo.CategoryVO;
import com.example.result.Result;
import com.example.result.ResultCodeEnum;
import com.example.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/tree")
    public Result<List<CategoryTreeVO>> getCategoryTree() {
        return Result.ok(categoryService.getCategoryTree());
    }

    @GetMapping("/list")
    public Result<List<CategoryVO>> getCategoryList() {
        List<CategoryVO> list = categoryService.listCategory();
        if (list == null) {
            return Result.build(null, ResultCodeEnum.DATA_NOT_FOUND);
        }
        return Result.ok(list);
    }
}
