package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Product;

import java.util.List;

public interface ProductService extends IService<Product> {

    List<Product> savelist();
}
