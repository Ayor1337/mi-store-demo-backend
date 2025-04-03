package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Product;
import com.example.entity.vo.request.ReqProductVO;
import com.example.mapper.ProductMapper;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {


    @Override
    public void saveProduct(ReqProductVO vo) {
        Product product = new Product();
        product.setName(vo.getName());
        product.setDescription(vo.getDescription());
        product.setPrice(vo.getPrice());
        product.setStock(vo.getStock());
        product.setCategoryId(vo.getCategoryId());
        this.save(product);
    }

    @Override
    public void deleteByIds(Integer id) {
        this.deleteByIds(id);
    }
}
