package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.pojo.Cart;
import com.example.entity.vo.CartVO;
import com.example.mapper.CartMapper;
import com.example.service.CartItemService;
import com.example.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {


    @Autowired
    CartItemService cartItemService;

    @Override
    public CartVO getCartVOByUserId(Integer userId) {
        CartVO cartVO = new CartVO();
        Integer cartId = this.lambdaQuery().eq(Cart::getUserId, userId).one().getCartId();
        cartVO.setCartId(cartId);
        cartVO.setCartItems(cartItemService.getCartItemVOByCartId(cartId));
        return cartVO;
    }
}
