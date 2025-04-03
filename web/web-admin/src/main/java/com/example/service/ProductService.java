package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Product;
import com.example.entity.vo.request.ReqProductVO;
import com.example.entity.vo.response.RespProductVO;

public interface ProductService extends IService<Product> {
    void saveProduct(ReqProductVO vo);

    void deleteById(Integer id);

    RespProductVO getProductById(Integer id);
}
