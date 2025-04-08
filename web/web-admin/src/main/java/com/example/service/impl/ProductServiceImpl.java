package com.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.ProductDTO;
import com.example.entity.pojo.Product;
import com.example.entity.vo.ProductVO;
import com.example.mapper.ProductMapper;
import com.example.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {


    @Override
    public List<ProductVO> listProduct() {
        List<Product> list = this.list();
        return list.stream().map(product -> {
            ProductVO productVO = new ProductVO();
            BeanUtils.copyProperties(product, productVO);
            return productVO;
        }).toList();
    }

    @Override
    @Transactional
    public String saveProduct(ProductDTO vo) {
        if (vo == null) {
            return "发送数据为空";
        }

        Product product = new Product();
        product.setName(vo.getName());
        product.setDescription(vo.getDescription());
        product.setPrice(vo.getPrice());
        product.setStock(vo.getStock());
        product.setCategoryId(vo.getCategoryId());
        this.save(product);

        return null;
    }

    @Override
    @Transactional
    public String deleteById(Integer id) {
        if (!existProductById(id)) {
            return "商品不存在";
        }
        this.removeById(id);
        return null;
    }

    public boolean existProductById(Integer id) {
        return this.exists(Wrappers.<Product>lambdaQuery().eq(Product::getProductId, id));
    }

    @Override
    public ProductVO getProductById(Integer id) {
        Product product = this.lambdaQuery().eq(Product::getProductId, id).one();
        if (product == null) {
            return null;
        }
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(product, productVO);
        return productVO;
    }

    @Override
    @Transactional
    public String updateProduct(ProductDTO dto) {
        if (dto == null) {
            return "修改的对象为空";
        }
        Product product = new Product();
        product.setProductId(dto.getProductId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setCategoryId(dto.getCategoryId());
        this.updateById(product);

        return null;
    }

    @Override
    @Transactional
    public String increaseProductStock(Integer productId, Integer quantity) {
        Product product = this.getById(productId);
        if (quantity == null || quantity <= 0) {
            return "购物数量必须大于0";
        }
        if (product == null) {
            return "商品不存在";
        }
        product.setStock(product.getStock() + quantity);
        this.updateById(product);
        return null;
    }

    @Override
    @Transactional
    public String decreaseProductStock(Integer productId, Integer quantity) {
        Product product = this.getById(productId);
        if (quantity == null || quantity <= 0) {
            return "购物数量必须大于0";
        }
        if (product == null) {
            return "商品不存在";
        }
        if (product.getStock() < quantity) {
            return "库存不足";
        }
        product.setStock(product.getStock() - quantity);
        this.updateById(product);
        return null;
    }
}
