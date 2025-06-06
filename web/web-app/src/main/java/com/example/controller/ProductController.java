package com.example.controller;

import com.example.entity.app.vo.CommodityVO;
import com.example.entity.app.vo.ProductVO;
import com.example.result.Result;
import com.example.service.CommodityService;
import com.example.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Resource
    private ProductService productService;

    @Resource
    private CommodityService commodityService;

    @GetMapping("/info/list_latest_products")
    public Result<List<ProductVO>> listLatestProducts(@RequestParam("categoryId") Integer categoryId) {
        return Result.dataMessageHandler(() -> productService.getLatestProductsByCategoryId(categoryId), "获取最新商品失败");
    }

    @GetMapping("/info/get_commodity")
    public Result<List<CommodityVO>> getCommodity(@RequestParam("productId") Integer productId) {
        return Result.dataMessageHandler(() -> commodityService.getCommoditiesByProductId(productId), "获取商品失败");
    }

    @GetMapping("/info/get_commodity_by_id")
    public Result<CommodityVO> getCommodityById(@RequestParam("commodityId") Integer commodityId) {
        return Result.dataMessageHandler(() -> commodityService.getCommodityVOById(commodityId), "获取商品失败");
    }

    @GetMapping("/info/search")
    public Result<List<CommodityVO>> search(@RequestParam("keyword") String keyword) {
        return Result.dataMessageHandler(() -> commodityService.getCommoditiesByKeyword(keyword), "搜索失败");
    }
}
