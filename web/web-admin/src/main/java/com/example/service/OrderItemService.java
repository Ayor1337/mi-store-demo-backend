package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.admin.dto.OrderItemDTO;
import com.example.entity.admin.vo.OrderItemVO;
import com.example.entity.pojo.OrderItem;

import java.util.List;

public interface OrderItemService extends IService<OrderItem> {
    List<OrderItemVO> getOrderItemVOsByOrderId(Integer orderId);

    String deleteOrderItemById(Integer orderItemId);

    String saveOrderItem(OrderItemDTO orderItemDTO);

    String increaseOrderItemQuantity(Integer orderItemId, Integer quantity);

    String decreaseOrderItemQuantity(Integer orderItemId, Integer quantity);

    String changeOrderItemQuantity(Integer orderItemId, Integer quantity);
}
