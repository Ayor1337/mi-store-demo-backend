package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.pojo.Cart;
import com.example.entity.vo.CartVO;

public interface CartService extends IService<Cart> {
    CartVO getCartVOByUserId(Integer userId);

    String saveCart(Integer userId);

    String deleteCartByUserId(Integer userId);
}
