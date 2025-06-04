package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.app.vo.CartItemVO;
import com.example.entity.pojo.CartItem;
import com.example.entity.pojo.Commodity;
import com.example.entity.pojo.Product;
import com.example.mapper.CartItemMapper;
import com.example.service.CartItemService;
import com.example.service.CommodityService;
import com.example.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CartItemServiceImpl extends ServiceImpl<CartItemMapper, CartItem> implements CartItemService {


    @Resource
    private CommodityService commodityService;

    @Resource
    private ProductService productService;

    @Override
    public List<CartItemVO> getCartItemsByCartId(Integer cartId) {
        List<CartItemVO> cartItemVOList = new ArrayList<>();
        List<CartItem> cartItems = this.lambdaQuery().eq(CartItem::getCartId, cartId).list();
        return getCartItemVOS(cartItems, cartItemVOList);
    }

    @Override
    public Integer getQuantityById(Integer cartItemId) {
        return this.lambdaQuery()
                .eq(CartItem::getCartItemId, cartItemId)
                .one()
                .getQuantity();
    }

    @Override
    public Integer getCommodityIdById(Integer cartItemId) {
        return this.lambdaQuery()
                .eq(CartItem::getCartItemId, cartItemId)
                .one()
                .getCommodityId();
    }

    @Override
    public String updateCartItemQuantity(Integer cartItemId, Integer quantity) {
        if (quantity < 1) {
            return "商品数量不能小于1";
        }
        CartItem cartItem = this.lambdaQuery()
                .eq(CartItem::getCartItemId, cartItemId)
                .one();
        cartItem.setQuantity(quantity);
        this.updateById(cartItem);
        return null;
    }

    @Override
    public String deleteCartItem(Integer cartItemId, Integer cartId) {
        CartItem cartItem = this.lambdaQuery()
                .eq(CartItem::getCartItemId, cartItemId)
                .eq(CartItem::getCartId, cartId)
                .one();
        if (cartItem == null)
            return "购物车中没有此商品";
        this.removeById(cartItem);
        return null;
    }

    @Override
    public String checkinCartItemById(Integer cartItemId) {
        CartItem item = this.getById(cartItemId);
        if (item == null) {
            return "购物车中没有此商品";
        }
        item.setIsChecked(true);
        this.updateById(item);
        return null;
    }

    @Override
    public String checkoutCartItemById(Integer cartItemId) {
        CartItem item = this.getById(cartItemId);
        if (item == null) {
            return "购物车中没有此商品";
        }
        item.setIsChecked(false);
        this.updateById(item);
        return null;
    }

    @Override
    public List<CartItemVO> getCheckedItemsByCartId(Integer cartId) {
        List<CartItem> cartItems = this.lambdaQuery()
                .eq(CartItem::getCartId, cartId)
                .eq(CartItem::getIsChecked, true)
                .list();
        List<CartItemVO> cartItemVOList = new ArrayList<>();
        return getCartItemVOS(cartItems, cartItemVOList);
    }

    @Override
    public boolean removeCartItemByIds(List<Integer> cartItemId) {
        return this.removeBatchByIds(cartItemId);
    }

    private List<CartItemVO> getCartItemVOS(List<CartItem> cartItems, List<CartItemVO> cartItemVOList) {
        cartItems.forEach(cartItem -> {
            CartItemVO cartItemVO = new CartItemVO();
            BeanUtils.copyProperties(cartItem, cartItemVO);
            Commodity commodity = commodityService.getById(cartItem.getCommodityId());
            Product product = productService.getById(commodity.getProductId());
            cartItemVOList.add(cartItemVO);
            cartItemVO.setTotalPrice(commodity.getPrice().multiply(new BigDecimal(cartItemVO.getQuantity())));
            cartItemVO.setSku(commodity.getFullName());
            cartItemVO.setPrice(commodity.getPrice());
            cartItemVO.setDescription(commodity.getDescription());
            cartItemVO.setImageUrl(product.getImageUrl());
        });

        return cartItemVOList;
    }

}
