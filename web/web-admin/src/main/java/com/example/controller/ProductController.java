package com.example.controller;

import com.example.entity.dto.Category;
import com.example.entity.dto.Product;
import com.example.entity.vo.request.ReqProductVO;
import com.example.result.Result;
import com.example.service.CategoryService;
import com.example.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Lang;
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
    public Result<List<Product>> getProductsAsList(){
        List<Product> list=productService.list();
        return Result.ok(list);
    }

    @GetMapping("/category/list")
    public Result<List<Category>> getCategoryList() {
        List<Category> list = categoryService.list();
        return Result.ok(list);
    }

    @PostMapping("/save_product")
    public Result<String> saveProduct(@RequestBody ReqProductVO vo) {
        productService.saveProduct(vo);
        return Result.ok("保存成功");
    }


    @DeleteMapping("/delete/{id}")
    public Result<String> deleteProduct(@PathVariable Integer id) {
        log.info("删除商品id：{}",id);
        productService.deleteByIds(id);

        return Result.ok("删除成功");
    }
}
