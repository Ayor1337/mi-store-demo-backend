package com.example.controller;

import com.example.entity.admin.dto.CommodityDTO;
import com.example.entity.admin.vo.CommodityVO;
import com.example.entity.admin.vo.CommodityWithFullNameVO;
import com.example.result.Result;
import com.example.service.CommodityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/commodity")
@Tag(name = "商品详情管理", description = "商品相关的接口")
public class CommodityController {

    @Autowired
    private CommodityService commodityService;

    @GetMapping("/list")
    @Operation(summary = "获取商品列表", description = "获取所有商品的详细信息")
    public Result<List<CommodityVO>> list() {
        return Result.dataMessageHandler(() -> commodityService.getCommodityVOs(), "商品列表获取失败");
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "根据ID获取商品", description = "根据商品ID获取商品详细信息")
    @Parameter(name = "id", description = "商品ID", required = true)
    public Result<CommodityVO> getCommodityById(@PathVariable Integer id) {
        return Result.dataMessageHandler(() -> commodityService.getCommodityById(id), "商品获取失败");
    }

    @GetMapping("/list_by_productId/{productId}")
    @Operation(summary = "根据产品ID获取商品列表", description = "根据产品ID获取商品列表")
    @Parameter(name = "productId", description = "产品ID", required = true)
    public Result<List<CommodityVO>> listByProductId(@PathVariable Integer productId) {
        return Result.dataMessageHandler(() -> commodityService.getCommodityVOsByProductId(productId), "商品列表获取失败");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除商品", description = "根据商品ID删除商品")
    @Parameter(name = "id", description = "商品ID", required = true)
    public Result<Void> deleteById(@PathVariable Integer id) {
        return Result.messageHandler(() -> commodityService.deleteCommodityById(id));
    }

    @PostMapping("/save")
    @Operation(summary = "保存商品", description = "保存新的商品信息")
    public Result<Void> saveCommodity(@RequestBody CommodityDTO commodityDTO) {
        return Result.messageHandler(() -> commodityService.saveCommodity(commodityDTO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新商品", description = "更新现有商品信息")
    public Result<Void> updateCommodity(@RequestBody CommodityDTO commodityDTO) {
        return Result.messageHandler(() -> commodityService.updateCommodity(commodityDTO));
    }

    @GetMapping("/get_name_list_by_product_id/{productId}")
    @Operation(summary = "根据产品ID获取商品名称列表", description = "根据产品ID获取商品名称列表")
    @Parameter(name = "productId", description = "产品ID", required = true)
    public Result<List<CommodityWithFullNameVO>> getNameListByProductId(@PathVariable Integer productId) {
        return Result.dataMessageHandler(() -> commodityService.getFullNameListByProductId(productId), "商品列表获取失败");
    }


}