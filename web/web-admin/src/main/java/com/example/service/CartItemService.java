package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.CartItemDTO;
import com.example.entity.pojo.CartItem;
import com.example.entity.vo.CartItemVO;

import java.util.List;

public interface CartItemService extends IService<CartItem> {
    List<CartItemVO> getCartItemVOByCartId(Integer cartId);

    String saveCartItem(CartItemDTO dto);

    String deleteCartItemById(Integer cartItemId);

    String deleteAllCartItemByCartId(Integer userId);

    String deleteAllCartItemByProductId(Integer productId);

    String updateCartItemById(CartItemDTO dto);

    String increaseCartItemQuantity(Integer cartItemId, Integer quantity);

    String decreaseCartItemQuantity(Integer cartItemId, Integer quantity);

    String changeCartItemQuantity(Integer cartItemId, Integer quantity);
}
