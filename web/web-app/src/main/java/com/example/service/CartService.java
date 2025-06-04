package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.app.vo.CartItemVO;
import com.example.entity.pojo.Cart;

import java.util.List;

public interface CartService extends IService<Cart> {

    String createCart(Integer userId);

    List<CartItemVO> getCartByUserId(Integer userId);

    String addCartItem(Integer commodityId, Integer quantity, Integer userId);

    Integer getCartIdByUserId(Integer userId);

    String submitCart(List<Integer> userId);

    String checkinById(Integer cartItemId, Integer userId);

    String checkoutById(Integer cartItemId, Integer userId);

    List<CartItemVO> getCheckoutsByUserId(Integer userId);
}
