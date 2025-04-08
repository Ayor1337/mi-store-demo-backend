package com.example.controller;

import com.example.entity.dto.ProductDTO;
import com.example.entity.vo.ProductVO;
import com.example.result.Result;
import com.example.result.ResultCodeEnum;
import com.example.service.CategoryService;
import com.example.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    @Description("获取所有商品")
    public Result<List<ProductVO>> getProductsAsList() {
        List<ProductVO> productVO = productService.listProduct();
        if (productVO == null)
            return Result.build(null, ResultCodeEnum.DATA_NOT_FOUND);
        return Result.ok(productVO);
    }


    @PostMapping("/save_product")
    public Result<Void> saveProduct(@RequestBody ProductDTO dto) {
        return Result.messageHandler(() -> productService.saveProduct(dto));
    }


    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteProduct(@PathVariable Integer id) {
        log.info("删除商品id：{}", id);
        return Result.messageHandler(() -> productService.deleteById(id));
    }

    @GetMapping("/get_product/{id}")
    public Result<ProductVO> getProduct(@PathVariable Integer id) {
        log.info("查询商品回写id：{}", id);
        ProductVO product = productService.getProductById(id);
        if (product == null)
            return Result.fail(400, "商品不存在");
        return Result.ok(product);
    }

    @PostMapping("/update")
    public Result<Void> updateProduct(@RequestBody ProductDTO dto) {
        return Result.messageHandler(() -> productService.updateProduct(dto));
    }

    @PostMapping("/increase_stock")
    public Result<Void> increaseStock(@RequestParam Integer productId, @RequestParam Integer quantity) {
        return Result.messageHandler(() -> productService.increaseProductStock(productId, quantity));
    }

    @PostMapping("/decrease_stock")
    public Result<Void> decreaseStock(@RequestParam Integer productId, @RequestParam Integer quantity) {
        return Result.messageHandler(() -> productService.decreaseProductStock(productId, quantity));
    }


}
