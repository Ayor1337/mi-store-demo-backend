package com.example.listener;

import com.example.entity.message.SubmitOrderMessage;
import com.example.entity.pojo.CartItem;
import com.example.entity.pojo.Commodity;
import com.example.entity.pojo.Order;
import com.example.entity.pojo.OrderItem;
import com.example.service.CartItemService;
import com.example.service.CommodityService;
import com.example.service.OrderItemService;
import com.example.service.OrderService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
@RabbitListener(queues = "submit_order_queue")
public class SubmitOrderListener {

    @Resource
    @Lazy
    private RabbitTemplate rabbitTemplate;

    @Resource
    private OrderItemService orderItemService;

    @Resource
    private OrderService orderService;

    @Resource
    private CartItemService cartItemService;

    @Resource
    private CommodityService commodityService;

    @RabbitHandler
    @Transactional(rollbackFor = Exception.class)
    public void createOrder(SubmitOrderMessage message) {
        try {
            BigDecimal totalPrice = new BigDecimal(0);
            List<CartItem> cartItems = cartItemService.list();
            List<CartItem> submitCartItem = cartItems
                    .stream()
                    .filter(cartItem -> message.getCartItemIds()
                            .contains(cartItem.getCartItemId()))
                    .toList();
            if (submitCartItem.isEmpty()) {
                return;
            }

            Order order = new Order();
            BeanUtils.copyProperties(message.getOrderDTO(), order);
            order.setTotalPrice(new BigDecimal(0));
            orderService.save(order);

            for (CartItem cartItem : submitCartItem) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(order.getOrderId());
                Commodity commodity = commodityService.getById(cartItem.getCommodityId());
                orderItem.setCommodityId(cartItem.getCommodityId());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPrice(commodity.getPrice());
                totalPrice = totalPrice.add(commodity.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
                orderItemService.save(orderItem);
            }
            order.setTotalPrice(totalPrice);
            cartItemService.removeBatchByIds(message.getCartItemIds());
            orderService.updateById(order);
        } catch (Exception e) {
            try {
                rabbitTemplate.execute(channel -> {
                    channel.queuePurge("mail");
                    return null;
                });
                throw e;
            } catch (Exception purgeException) {
                log.warn("清空邮件队列失败：{}", purgeException.getMessage());
                throw e;
            }
        }

    }
}
