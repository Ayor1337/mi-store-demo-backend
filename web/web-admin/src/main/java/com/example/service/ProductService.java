package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.ProductDTO;
import com.example.entity.pojo.Product;
import com.example.entity.vo.ProductVO;

import java.util.List;

public interface ProductService extends IService<Product> {
    List<ProductVO> listProduct();

    String saveProduct(ProductDTO vo);

    String deleteById(Integer id);

    ProductVO getProductById(Integer id);

    String updateProduct(ProductDTO vo);

    String increaseProductStock(Integer productId, Integer quantity);

    String decreaseProductStock(Integer productId, Integer quantity);
}
