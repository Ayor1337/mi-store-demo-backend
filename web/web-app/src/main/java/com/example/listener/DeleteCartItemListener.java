package com.example.listener;

import com.example.entity.message.DeleteCartItemMessage;
import com.example.entity.message.result.DeleteCartItemResult;
import com.example.service.CommodityService;
import com.example.service.impl.CartItemServiceImpl;
import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RabbitListener(queues = "delete_cartItem_queue")
public class DeleteCartItemListener {

    @Resource
    private CartItemServiceImpl cartItemService;

    @Resource
    private CommodityService commodityService;

    @RabbitHandler
    public DeleteCartItemResult deleteCartItem(DeleteCartItemMessage payload,
                                               Message message,
                                               Channel channel) throws IOException {
        Integer cartItemId = payload.getCartItemId();
        Integer cartId = payload.getCartId();
        Integer quantity = cartItemService.getQuantityById(cartItemId);
        Integer commodityId = cartItemService.getCommodityIdById(cartItemId);

        if (cartItemId == null || cartId == null) {
            return new DeleteCartItemResult(false, "参数错误");
        }

        try {
            if (cartItemService.deleteCartItem(cartItemId, cartId) != null)
                return new DeleteCartItemResult(false, "删除失败");
            if (commodityService.increaseCommodityStock(commodityId, quantity) != null)
                return new DeleteCartItemResult(false, "删除失败");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            log.warn("删除购物车失败 : {}", e.getMessage());

        }

        return new DeleteCartItemResult(true, "删除成功");


    }
}
