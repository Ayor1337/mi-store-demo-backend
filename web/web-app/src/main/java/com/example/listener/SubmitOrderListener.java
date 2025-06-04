package com.example.listener;

import com.example.entity.app.vo.CartItemVO;
import com.example.entity.message.AppSubmitOrderMessage;
import com.example.entity.message.result.SubmitOrderResult;
import com.example.service.CartItemService;
import com.example.service.CartService;
import com.example.service.OrderItemService;
import com.example.service.OrderService;
import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RabbitListener(queues = "submit_cart_queue")
public class SubmitOrderListener {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private CartService cartService;

    @Resource
    private CartItemService cartItemService;

    @Resource
    private OrderService orderService;

    @Resource
    private OrderItemService orderItemService;

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    @RabbitHandler
    @SendTo
    private SubmitOrderResult submitOrder(AppSubmitOrderMessage payload,
                                          Message message,
                                          Channel channel) throws IOException {

        Integer userId = payload.getUserId();
        Integer addressId = payload.getAddressId();

        try {
            if (userId == null) {
                throw new RuntimeException("参数错误");
            }
            // 获取购物车id
            Integer cartId = cartService.getCartIdByUserId(userId);
            if (cartId == null) {
                throw new RuntimeException("用户购物车不存在");
            }
            // 获取购物车需要提交的商品
            List<CartItemVO> cartItemVOS = cartItemService.getCheckedItemsByCartId(cartId);
            List<Integer> cartItemIds = new ArrayList<>();
            for (CartItemVO cartItemVO : cartItemVOS) {
                cartItemIds.add(cartItemVO.getCartItemId());
            }

            Integer orderId = orderService.createNewOrder(userId, addressId);
            if (!orderItemService.saveOrderItemsByOrderId(orderId, cartId)) {
                throw new RuntimeException("提交失败2");
            }

            if (!orderService.initOrder(orderId)) {
                throw new RuntimeException("提交失败3");
            }

            if (!cartItemService.removeCartItemByIds(cartItemIds)) {
                throw new RuntimeException("提交失败1");
            }

            redisTemplate.opsForValue().set("pendingOrder:" + orderId, "", 1, TimeUnit.DAYS);

            rabbitTemplate.convertAndSend("pay", "pay", orderId);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return new SubmitOrderResult(true, "提交成功", orderId);


        } catch (Exception e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            return new SubmitOrderResult(false, e.getMessage(), null);
        }
    }

}
