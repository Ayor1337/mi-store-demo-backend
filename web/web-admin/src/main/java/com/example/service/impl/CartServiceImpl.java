package com.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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

    /**
     * 根据用户ID获取购物车视图对象
     * <p>
     * 此方法首先会根据用户ID查询对应的购物车记录，以获取购物车ID
     * 然后，使用这个购物车ID来查询购物车中的所有商品项，并将其设置到购物车视图对象中
     *
     * @param userId 用户ID，用于查询购物车信息
     * @return 返回一个包含购物车ID和购物车商品项的购物车视图对象
     */
    @Override
    public CartVO getCartVOByUserId(Integer userId) {
        if (userId == null || !existsCartByUserId(userId)) {
            return null;
        }

        // 创建一个购物车视图对象实例
        CartVO cartVO = new CartVO();

        // 查询用户ID对应的购物车记录，并获取该购物车的ID
        Integer cartId = this.lambdaQuery().eq(Cart::getUserId, userId).one().getCartId();

        // 将获取到的购物车ID设置到购物车视图对象中
        cartVO.setCartId(cartId);

        // 查询并设置购物车中的所有商品项
        cartVO.setCartItems(cartItemService.getCartItemVOByCartId(cartId));

        // 返回填充好的购物车视图对象
        return cartVO;
    }

    private boolean existsCartByUserId(Integer userId) {
        return this.baseMapper.exists(Wrappers.<Cart>lambdaQuery().eq(Cart::getUserId, userId));
    }
}
