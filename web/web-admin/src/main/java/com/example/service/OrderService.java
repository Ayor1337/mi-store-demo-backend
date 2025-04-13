package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.OrderDTO;
import com.example.entity.pojo.Order;
import com.example.entity.vo.OrderVO;

import java.util.List;

public interface OrderService extends IService<Order> {
    List<OrderVO> getAllOrders();

    List<OrderVO> getOrdersByUserId(Integer userId);

    String deleteOrdersByUserId(Integer userId);

    String saveOrder(OrderDTO dto);
}
