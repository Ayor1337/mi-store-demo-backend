package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.entity.PayOrder;
import com.example.entity.pojo.Order;
import com.example.entity.pojo.OrderItem;
import com.example.entity.pojo.PaymentRecord;
import com.example.mapper.OrderMapper;
import com.example.service.CommodityService;
import com.example.service.OrderItemService;
import com.example.service.OrderService;
import com.example.service.PaymentRecordService;
import com.rabbitmq.client.GetResponse;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    OrderItemService orderItemService;

    @Resource
    CommodityService commodityService;

    @Resource
    PaymentRecordService paymentRecordService;

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    @Resource
    private RabbitTemplate rabbitTemplate;

    private void consumePayQueue(Integer orderId) {
        rabbitTemplate.execute(channel -> {
            List<GetResponse> cached = new ArrayList<>();
            GetResponse response = channel.basicGet("pay_queue", false);
            while (response != null) {
                String body = new String(response.getBody(), StandardCharsets.UTF_8);
                if (body.equals(orderId.toString())) {
                    channel.basicAck(response.getEnvelope().getDeliveryTag(), false);
                } else {
                    cached.add(response);
                }
                response = channel.basicGet("pay_queue", false);
            }
            for (GetResponse other : cached) {
                channel.basicPublish("", "pay_queue", other.getProps(), other.getBody());
                channel.basicAck(other.getEnvelope().getDeliveryTag(), false);
            }
            return null;
        });
    }

    @Override
    public PayOrder createPayOrder(Integer orderId) {
        Order order = this.getById(orderId);
        String key = "pendingOrder:" + orderId;
        Long remainMs = redisTemplate.getExpire(key);
        if (remainMs <= 0) {
            return null;
        }

        List<OrderItem> orderItems = orderItemService.lambdaQuery().eq(OrderItem::getOrderId, orderId).list();

        PayOrder payOrder = new PayOrder();

        payOrder.setOrderId(order.getOrderId().toString());

        if (orderItems.size() == 1) {
            payOrder.setSubject(commodityService.getById(orderItems.get(0).getCommodityId()).getFullName());
        } else {
            String subject = "共计" +
                    orderItems.size() +
                    "件商品";
            payOrder.setSubject(subject);
        }

        BigDecimal totalPrice = new BigDecimal("0");
        for (OrderItem orderItem : orderItems) {
            totalPrice = totalPrice.add(orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
        }
        payOrder.setPrice(totalPrice.toString());
        return payOrder;
    }

    @Override
    public String changePaymentStatus(Map<String, String[]> payResponse) {
        String payment_time = Arrays.toString(payResponse.get("gmt_payment"));
        String transaction_id = Arrays.toString(payResponse.get("trade_no"));
        String order_id = Arrays.toString(payResponse.get("out_trade_no"));
        String amount = Arrays.toString(payResponse.get("total_amount"));
        this.lambdaUpdate()
                .eq(Order::getOrderId, Integer.parseInt(order_id.replaceAll("[\\[\\] ]", "")))
                .set(Order::getPaymentStatus, "已支付")
                .set(Order::getUpdateTime, new Date())
                .update();
        PaymentRecord paymentRecord = new PaymentRecord();
        paymentRecord.setOrderId(Integer.parseInt(order_id.replaceAll("[\\[\\] ]", "")));
        paymentRecord.setPaymentMethod("支付宝");
        paymentRecord.setAmount(new BigDecimal(amount.replaceAll("[\\[\\] ]", "")));
        paymentRecord.setTransactionId(transaction_id.replaceAll("[\\[\\] ]", ""));
        SimpleDateFormat dateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
        try {
            paymentRecord.setPaymentTime(dateFormat.parse(payment_time));
        } catch (Exception e) {
            e.printStackTrace();
        }
        paymentRecordService.createPaymentRecord(paymentRecord);
        consumePayQueue(Integer.parseInt(order_id.replaceAll("[\\[\\] ]", "")));
        return null;


    }


}
