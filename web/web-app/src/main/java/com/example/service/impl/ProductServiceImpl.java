package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.app.vo.ProductVO;
import com.example.entity.pojo.Product;
import com.example.mapper.ProductMapper;
import com.example.service.CommodityService;
import com.example.service.ProductService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Resource
    private CommodityService commodityService;

    @Override
    public List<ProductVO> getLatestProductsByCategoryId(Integer categoryId) {
        List<Product> latestProductList = this.lambdaQuery()
                .eq(Product::getCategoryId, categoryId)
                .orderByDesc(Product::getProductId)
                .last("limit 8")
                .list();
        List<ProductVO> productVOS = new ArrayList<>();
        latestProductList.forEach(product -> {
            ProductVO productVO = new ProductVO();
            BigDecimal price = commodityService.getLowestPriceByProductId(product.getProductId());
            BeanUtils.copyProperties(product, productVO);
            productVO.setPrice(price);
            productVOS.add(productVO);
        });
        return productVOS;
    }


}
