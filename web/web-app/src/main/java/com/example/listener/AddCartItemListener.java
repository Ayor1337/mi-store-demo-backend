package com.example.listener;

import com.example.entity.message.AddCartItemMessage;
import com.example.entity.message.result.AddCartItemResult;
import com.example.service.CartService;
import com.example.service.CommodityService;
import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RabbitListener(queues = "add_cartItem_queue")
public class AddCartItemListener {

    @Resource
    private CartService cartService;

    @Resource
    private CommodityService commodityService;

    @RabbitHandler
    @SendTo
    public AddCartItemResult addCartItem(AddCartItemMessage payload,
                                         Message message,
                                         Channel channel) throws IOException {
        Integer userId = payload.getUserId();
        Integer commodityId = payload.getCommodityId();
        Integer quantity = payload.getQuantity();

        if (userId == null || commodityId == null || quantity == null) {
            return new AddCartItemResult(userId, commodityId, false, "参数有误");
        }

        try {
            if (commodityService.decreaseCommodityStock(commodityId, quantity) != null) {
                return new AddCartItemResult(userId, commodityId, false, "商品库存不足");
            }
            if (cartService.addCartItem(commodityId, quantity, userId) != null) {
                return new AddCartItemResult(userId, commodityId, false, "商品添加失败");
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.warn("添加购物车失败 : {}", e.getMessage());
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
        return new AddCartItemResult(userId, commodityId, true, "添加购物车成功");
    }


}
