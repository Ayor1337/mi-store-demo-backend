package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.pojo.CartItem;
import com.example.entity.vo.CartItemVO;

import java.util.List;

public interface CartItemService extends IService<CartItem> {
    List<CartItemVO> getCartItemVOByCartId(Integer cartId);
}
