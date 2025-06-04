package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.app.vo.OrderItemVO;
import com.example.entity.pojo.OrderItem;

import java.util.List;

public interface OrderItemService extends IService<OrderItem> {
    boolean saveOrderItemsByOrderId(Integer orderId, Integer cartId);

    List<OrderItem> getOrderItemsByOrderId(Integer orderId);

    List<OrderItemVO> getOrderItemVOByOrderId(Integer orderId);
}
