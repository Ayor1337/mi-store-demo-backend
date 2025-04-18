package com.example.controller;

import com.example.entity.dto.Base64Upload;
import com.example.entity.dto.ProductDTO;
import com.example.entity.vo.ProductVO;
import com.example.result.Result;
import com.example.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@Tag(name = "商品管理", description = "与商品相关的操作")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    @Operation(summary = "获取所有商品", description = "获取商品列表")
    public Result<List<ProductVO>> getProductsAsList() {
        return Result.dataMessageHandler(() -> productService.listProduct(), "获取商品列表失败");
    }

    @PostMapping("/save_product")
    @Operation(summary = "保存商品", description = "创建一个新的商品")
    public Result<Void> saveProduct(@RequestBody @Valid @Parameter(description = "商品详情") ProductDTO dto) {
        return Result.messageHandler(() -> productService.saveProduct(dto));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除商品", description = "根据ID删除商品")
    public Result<Void> deleteProduct(@PathVariable @Parameter(description = "商品ID") Integer id) {
        log.info("删除商品id：{}", id);
        return Result.messageHandler(() -> productService.deleteProductById(id));
    }

    @GetMapping("/get_product/{id}")
    @Operation(summary = "获取商品详情", description = "根据ID获取商品详情")
    public Result<ProductVO> getProduct(@PathVariable @Parameter(description = "商品ID") Integer id) {
        log.info("查询商品回写id：{}", id);
        return Result.dataMessageHandler(() -> productService.getProductById(id), "商品不存在");
    }

    @GetMapping("/get_product_by_cid/{categoryId}")
    @Operation(summary = "根据分类ID获取商品", description = "根据分类ID获取商品列表")
    public Result<List<ProductVO>> getProductByCid(@PathVariable @Parameter(description = "分类ID") Integer categoryId) {
        return Result.dataMessageHandler(() -> productService.getProductByCategoryId(categoryId), "商品不存在");
    }

    @PostMapping("/update")
    @Operation(summary = "更新商品", description = "更新现有商品信息")
    public Result<Void> updateProduct(@RequestBody @Valid @Parameter(description = "更新后的商品详情") ProductDTO dto) {
        return Result.messageHandler(() -> productService.updateProduct(dto));
    }

    @PostMapping("/upload_img/{productId}")
    @Operation(summary = "上传商品图片", description = "为商品上传图片")
    public Result<Void> uploadImg(@RequestBody @Parameter(description = "Base64编码的图片") Base64Upload file, @PathVariable @Parameter(description = "商品ID") Integer productId) {
        return Result.messageHandler(() -> productService.updateImg(productId, file));
    }

    @GetMapping("/get_img/{productId}")
    @Operation(summary = "获取商品图片URL", description = "获取商品的图片URL")
    public Result<String> getImgUrl(@PathVariable @Parameter(description = "商品ID") Integer productId) {
        return Result.dataMessageHandler(() -> productService.getProductById(productId).getImageUrl(), "商品图片不存在");
    }

    @DeleteMapping("/del_img/{productId}")
    @Operation(summary = "删除商品图片", description = "删除商品的图片")
    public Result<Void> deleteImg(@PathVariable @Parameter(description = "商品ID") Integer productId) {
        return Result.messageHandler(() -> productService.deleteImg(productId));
    }
}