package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.pojo.Product;
import com.example.entity.dto.ProductDTO;
import com.example.entity.vo.ProductVO;

public interface ProductService extends IService<Product> {
    String saveProduct(ProductDTO vo);

    String deleteById(Integer id);

    ProductVO getProductById(Integer id);

    String updateProduct(ProductDTO vo);
}
