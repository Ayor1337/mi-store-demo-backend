package com.example.controller;

import com.example.entity.dto.CategoryDTO;
import com.example.entity.vo.CategoryVO;
import com.example.result.Result;
import com.example.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@Tag(name = "分类管理", description = "与商品分类相关的操作")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    @Operation(summary = "获取分类列表", description = "获取所有商品分类")
    public Result<List<CategoryVO>> getCategoryList() {
        return Result.dataMessageHandler(() -> categoryService.listCategory(), "获取分类列表失败");
    }

    @PostMapping("/save_category")
    @Operation(summary = "保存分类", description = "创建新的商品分类")
    public Result<Void> saveCategory(@RequestBody @Parameter(description = "分类详情") CategoryDTO dto) {
        return Result.messageHandler(() -> categoryService.saveCategory(dto));
    }

    @GetMapping("/get_category/{id}")
    @Operation(summary = "获取分类详情", description = "根据ID获取分类详情")
    public Result<CategoryVO> getCategory(@PathVariable @Parameter(description = "分类ID") Integer id) {
        return Result.dataMessageHandler(() -> categoryService.getCategoryById(id), "获取分类失败");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除分类", description = "根据ID删除分类")
    public Result<Void> deleteCategory(@PathVariable @Parameter(description = "分类ID") Integer id) {
        return Result.messageHandler(() -> categoryService.removeCategoryById(id));
    }
}