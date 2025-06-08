package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.app.vo.OrderDetailVO;
import com.example.entity.app.vo.OrderVO;
import com.example.entity.app.vo.PayConfirmVO;
import com.example.entity.pojo.Order;

import java.util.List;


public interface OrderService extends IService<Order> {
    Integer createNewOrder(Integer userId, Integer addressId);

    boolean initOrder(Integer orderId);

    List<OrderVO> getOrderVOByUserId(Integer userId);

    boolean confirmOrderOwnerByUserId(Integer orderId, Integer userId);

    PayConfirmVO getPayConfirmVO(Integer orderId, Integer userId);

    OrderDetailVO getOrderDetailVOByOrderId(Integer orderId, Integer userId);
}
