package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.admin.dto.OrderDTO;
import com.example.entity.admin.vo.CartVO;
import com.example.entity.pojo.Cart;

import java.util.List;

public interface CartService extends IService<Cart> {
    CartVO getCartVOByUserId(Integer userId);

    String createCartByUserId(Integer userId);

    String deleteCartByUserId(Integer userId);

    String submitCart(OrderDTO orderDTO, List<Integer> cartItemIds);
}
