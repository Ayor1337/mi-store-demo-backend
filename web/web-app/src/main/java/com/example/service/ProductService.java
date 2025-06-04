package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.app.vo.ProductVO;
import com.example.entity.pojo.Product;

import java.util.List;

public interface ProductService extends IService<Product> {
    List<ProductVO> getLatestProductsByCategoryId(Integer categoryId);
}
