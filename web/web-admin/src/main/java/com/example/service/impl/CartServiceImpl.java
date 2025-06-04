package com.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.admin.dto.OrderDTO;
import com.example.entity.admin.vo.CartVO;
import com.example.entity.message.SubmitOrderMessage;
import com.example.entity.pojo.Cart;
import com.example.mapper.CartMapper;
import com.example.service.CartItemService;
import com.example.service.CartService;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Resource
    CartItemService cartItemService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    // 根据用户ID获取购物车视图对象
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
        cartVO.setCartItems(cartItemService.getCartItemVOsByCartId(cartId));

        // 返回填充好的购物车视图对象
        return cartVO;
    }

    // 为所有的新用户创建一个购物车
    @Override
    public String createCartByUserId(Integer userId) {
        if (userId == null) {
            return "用户ID为空";
        }
        if (existsCartByUserId(userId)) {
            return "该用户已存在购物车";
        }
        this.save(new Cart(null, userId, null));
        return null;

    }

    // 根据用户ID删除购物车
    @Override
    public String deleteCartByUserId(Integer userId) {
        if (userId == null || !existsCartByUserId(userId)) {
            return "用户ID为空或该用户不存在购物车";
        }
        Cart cart = this.lambdaQuery().eq(Cart::getUserId, userId).one();
        String messageFromDeleteAllCartItemByCartId = cartItemService.deleteAllCartItemByCartId(cart.getCartId());

        if (messageFromDeleteAllCartItemByCartId != null) {
            return messageFromDeleteAllCartItemByCartId;
        }

        this.removeById(cart.getCartId());
        return null;
    }


    // 提交购物车
    @Override
    public String submitCart(OrderDTO orderDTO, List<Integer> cartItemIds) {
        SubmitOrderMessage submitOrderMessage = new SubmitOrderMessage();
        submitOrderMessage.setOrderDTO(orderDTO);
        submitOrderMessage.setCartItemIds(cartItemIds);

        rabbitTemplate.convertAndSend("amq.direct", "submit_order", submitOrderMessage);


        return null;
    }

    // 判断用户是否存在购物车
    private boolean existsCartByUserId(Integer userId) {
        return this.baseMapper.exists(Wrappers.<Cart>lambdaQuery().eq(Cart::getUserId, userId));
    }
}
