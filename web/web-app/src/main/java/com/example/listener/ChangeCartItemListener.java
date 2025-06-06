package com.example.listener;

import com.example.entity.message.ChangeCartItemMessage;
import com.example.entity.message.result.ChangeCartItemResult;
import com.example.service.CartItemService;
import com.example.service.CartService;
import com.example.service.CommodityService;
import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component
@RabbitListener(queues = "update_cartItem_queue")
public class ChangeCartItemListener {

    @Resource
    private CartItemService cartItemService;

    @Resource
    private CartService cartService;

    @Resource
    private CommodityService commodityService;


    @Transactional(rollbackFor = Exception.class)
    @RabbitHandler
    @SendTo
    public ChangeCartItemResult changeCartItem(ChangeCartItemMessage payload,
                                               Message message,
                                               Channel channel) throws IOException {
        Integer cartItemId = payload.getCartItemId();
        Integer cartId = payload.getCartId();
        Integer quantity = payload.getQuantity();
        if (cartItemId == null || cartId == null || quantity == null) {
            return new ChangeCartItemResult(false, "参数错误");
        }
        try {
            Integer commodityId = cartItemService.getCommodityIdById(cartItemId);
            Integer oldQuantity = cartItemService.getQuantityById(cartItemId);
            if (quantity < 0) {
                throw new RuntimeException("数量不能小于0");
            }
            String result;
            if (quantity > oldQuantity) {
                result = commodityService.decreaseCommodityStock(commodityId, quantity - oldQuantity);
            } else {
                result = commodityService.increaseCommodityStock(commodityId, oldQuantity - quantity);
            }
            if (result != null)
                throw new RuntimeException(result);
            cartItemService.updateCartItemQuantity(cartItemId, quantity);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            return new ChangeCartItemResult(false, e.getMessage());
        }


        return new ChangeCartItemResult(true, "修改购物车成功");

    }
}
