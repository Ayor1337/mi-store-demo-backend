package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.app.vo.CartItemVO;
import com.example.entity.pojo.CartItem;

import java.util.List;

public interface CartItemService extends IService<CartItem> {

    List<CartItemVO> getCartItemsByCartId(Integer cartId);

    Integer getQuantityById(Integer cartItemId);

    Integer getCommodityIdById(Integer cartItemId);

    String updateCartItemQuantity(Integer cartItemId, Integer quantity);

    String deleteCartItem(Integer cartItemId, Integer cartId);

    String checkinCartItemById(Integer cartItemId);

    String checkoutCartItemById(Integer cartItemId);

    List<CartItemVO> getCheckedItemsByCartId(Integer cartId);


    boolean removeCartItemByIds(List<Integer> cartItemId);
}
