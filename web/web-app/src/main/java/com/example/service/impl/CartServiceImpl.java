package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.app.vo.CartItemVO;
import com.example.entity.pojo.Cart;
import com.example.entity.pojo.CartItem;
import com.example.mapper.CartMapper;
import com.example.service.CartItemService;
import com.example.service.CartService;
import com.example.service.CommodityService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Resource
    private CartItemService cartItemService;

    @Resource
    private CommodityService commodityService;

    @Override
    public String createCart(Integer userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        this.save(cart);
        return null;
    }

    @Override
    public List<CartItemVO> getCartByUserId(Integer userId) {
        Cart cart = this.lambdaQuery().eq(Cart::getUserId, userId).one();
        Integer cartId = cart.getCartId();

        return cartItemService.getCartItemsByCartId(cartId);
    }

    @Override
    public String addCartItem(Integer commodityId, Integer quantity, Integer userId) {
        if (userId == null) {
            return "请先登录";
        }
        if (commodityId == null)
            return "商品ID不能为空";
        if (quantity == null)
            return "商品数量不能为空";
        if (quantity <= 0) {
            return "商品数量必须大于0";
        }


        CartItem cartItem = new CartItem();
        cartItem.setQuantity(quantity);
        cartItem.setCartId(getCartIdByUserId(userId));
        cartItem.setCommodityId(commodityId);
        cartItemService.save(cartItem);
        return null;
    }

    @Override
    public Integer getCartIdByUserId(Integer userId) {
        return this.lambdaQuery().eq(Cart::getUserId, userId).one().getCartId();
    }

    @Override
    public String submitCart(List<Integer> userId) {
        return null;
    }

    @Override
    public String checkinById(Integer cartItemId, Integer userId) {
        Integer cartId = this.getCartIdByUserId(userId);
        if (cartId == null) {
            return "请先登录";
        }
        return cartItemService.checkinCartItemById(cartItemId);
    }

    @Override
    public String checkoutById(Integer cartItemId, Integer userId) {
        Integer cartId = this.getCartIdByUserId(userId);
        if (cartId == null) {
            return "请先登录";
        }
        return cartItemService.checkoutCartItemById(cartItemId);
    }

    @Override
    public List<CartItemVO> getCheckoutsByUserId(Integer userId) {
        Integer cartId = this.getCartIdByUserId(userId);
        if (cartId == null) {
            return null;
        }
        return cartItemService.getCheckedItemsByCartId(cartId);
    }

}
