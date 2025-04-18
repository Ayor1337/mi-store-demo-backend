package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.OrderDTO;
import com.example.entity.pojo.Cart;
import com.example.entity.vo.CartVO;

import java.util.List;

public interface CartService extends IService<Cart> {
    CartVO getCartVOByUserId(Integer userId);

    String saveCart(Integer userId);

    String deleteCartByUserId(Integer userId);

    String submitCart(OrderDTO orderDTO, List<Integer> cartItemIds);
}
