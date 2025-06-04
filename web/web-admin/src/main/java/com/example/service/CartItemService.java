package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.admin.dto.CartItemDTO;
import com.example.entity.admin.vo.CartItemVO;
import com.example.entity.pojo.CartItem;

import java.util.List;

public interface CartItemService extends IService<CartItem> {
    List<CartItemVO> getCartItemVOsByCartId(Integer cartId);

    String saveCartItem(CartItemDTO dto);

    String deleteCartItemById(Integer cartItemId);

    String batchDeleteByIds(List<Integer> ids);

    String deleteAllCartItemByCartId(Integer userId);

    String deleteAllCartItemByProductId(Integer productId);

    String updateCartItemById(CartItemDTO dto);

    String increaseCartItemQuantity(Integer cartItemId, Integer quantity);

    String decreaseCartItemQuantity(Integer cartItemId, Integer quantity);

    String changeCartItemQuantity(Integer cartItemId, Integer quantity);
}
