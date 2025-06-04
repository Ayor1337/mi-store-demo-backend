package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.admin.dto.OrderDTO;
import com.example.entity.admin.vo.OrderVO;
import com.example.entity.pojo.Order;

import java.util.List;


public interface OrderService extends IService<Order> {
    List<OrderVO> getAllOrders();

    OrderVO getOrderById(Integer orderId);

    List<OrderVO> getOrdersByUserId(Integer userId);

    String deleteOrdersByUserId(Integer userId);

    String deleteOrderById(Integer orderId);

    String saveOrder(OrderDTO dto);

    String updateOrder(OrderDTO dto);
}
