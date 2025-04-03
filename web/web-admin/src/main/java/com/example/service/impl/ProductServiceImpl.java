package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Product;
import com.example.entity.vo.request.ReqProductVO;
import com.example.entity.vo.response.RespProductVO;
import com.example.mapper.ProductMapper;
import com.example.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        this.removeById(id);
    }

    @Override
    public RespProductVO getProductById(Integer id) {
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Product::getProductId,id);
        Product list = this.getOne(queryWrapper);
        System.out.println(list);
        RespProductVO respProductVO = new RespProductVO();
        BeanUtils.copyProperties(list,respProductVO);
        return respProductVO;
    }
}
