package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.pojo.CartItem;
import com.example.entity.vo.CartItemVO;
import com.example.mapper.CartItemMapper;
import com.example.service.CartItemService;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Service
public class CartItemServiceImpl extends ServiceImpl<CartItemMapper, CartItem> implements CartItemService {

    @Autowired
    ProductService productService;

    @Override
    public List<CartItemVO> getCartItemVOByCartId(Integer cartId) {
        List<CartItemVO> cartItemVOList = new LinkedList<>();

        this.lambdaQuery()
                .eq(CartItem::getCartId, cartId)
                .list()
                .forEach(cartItem -> {
                    CartItemVO cartItemVO = new CartItemVO();
                    cartItemVO.setCartItemId(cartItem.getCartItemId());
                    cartItemVO.setName(productService.getById(cartItem.getProductId()).getName());
                    cartItemVO.setPrice(productService.getById(cartItem.getProductId()).getPrice());
                    cartItemVO.setQuantity(cartItem.getQuantity());
                    cartItemVO.setTotalPrice(cartItemVO.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
                    cartItemVOList.add(cartItemVO);
                });
        return cartItemVOList;
    }
}
