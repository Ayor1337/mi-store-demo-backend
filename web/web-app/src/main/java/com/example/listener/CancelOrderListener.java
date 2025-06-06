package com.example.listener;

import com.example.entity.pojo.Order;
import com.example.service.OrderService;
import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
@RabbitListener(queues = "pay_outdated_queue")
public class CancelOrderListener {

    @Resource
    private OrderService orderService;

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    @RabbitHandler
    public void cancelOrder(Integer orderId, Message message, Channel channel) throws IOException {
        try {
            // 删除过期订单标记
            redisTemplate.delete("pendingOrder:" + orderId);
            orderService.lambdaUpdate()
                    .eq(Order::getOrderId, orderId)
                    .set(Order::getStatus, "已取消")
                    .set(Order::getUpdateTime, new Date())
                    .update();
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
    }
}