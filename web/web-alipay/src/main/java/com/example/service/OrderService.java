package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.entity.PayOrder;
import com.example.entity.pojo.Order;

import java.util.Map;

public interface OrderService extends IService<Order> {

    PayOrder createPayOrder(Integer orderId);

    String changePaymentStatus(Map<String, String[]> payResponse);
}
