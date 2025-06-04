package com.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.admin.dto.OrderDTO;
import com.example.entity.admin.vo.DeliveryAddressVO;
import com.example.entity.admin.vo.OrderVO;
import com.example.entity.pojo.Order;
import com.example.entity.pojo.OrderItem;
import com.example.event.OrderPriceChangeEvent;
import com.example.mapper.OrderMapper;
import com.example.service.DeliveryAddressService;
import com.example.service.OrderItemService;
import com.example.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    private OrderItemService orderItemService;

    @Resource
    private DeliveryAddressService deliveryAddressService;

    @EventListener
    public void handleOrderPriceChangeEvent(OrderPriceChangeEvent event) {
        Order order = this.lambdaQuery().eq(Order::getOrderId, event.getOrderId()).one();
        BigDecimal totalPrice = new BigDecimal(0);
        for (OrderItem orderItem : orderItemService.lambdaQuery().eq(OrderItem::getOrderId, order.getOrderId()).list()) {
            totalPrice = totalPrice.add(orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
        }
        order.setUpdateTime(new Date());
        order.setTotalPrice(totalPrice);
        this.updateById(order);
    }

    @Override
    public List<OrderVO> getAllOrders() {
        List<OrderVO> voList = new ArrayList<>();
        this.list().forEach(order -> {
            OrderVO orderVO = new OrderVO();
            BigDecimal totalPrice = new BigDecimal(0);
            for (OrderItem orderItem : orderItemService.lambdaQuery().eq(OrderItem::getOrderId, order.getOrderId()).list()) {
                totalPrice = totalPrice.add(orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
            }
            DeliveryAddressVO deliveryAddressById = deliveryAddressService.getDeliveryAddressById(order.getAddressId());
            orderVO.setAddress(deliveryAddressById.getAddress());
            BeanUtils.copyProperties(order, orderVO);
            orderVO.setTotalPrice(totalPrice);
            voList.add(orderVO);
        });
        return voList;
    }

    @Override
    public OrderVO getOrderById(Integer orderId) {
        Order order = this.getById(orderId);
        if (order == null) {
            return null;
        }
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        DeliveryAddressVO deliveryAddressById = deliveryAddressService.getDeliveryAddressById(order.getAddressId());
        orderVO.setAddress(deliveryAddressById.getAddress());
        return orderVO;
    }

    @Override
    public List<OrderVO> getOrdersByUserId(Integer userId) {
        if (!existsOrdersByUserId(userId)) {
            return null;
        }
        List<OrderVO> voList = new ArrayList<>();
        this.lambdaQuery().eq(Order::getUserId, userId).list().forEach(order -> {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);
            DeliveryAddressVO deliveryAddressById = deliveryAddressService.getDeliveryAddressById(order.getAddressId());
            orderVO.setAddress(deliveryAddressById.getAddress());
            voList.add(orderVO);
        });
        return voList;
    }

    @Override
    public String deleteOrdersByUserId(Integer userId) {
        if (!existsOrdersByUserId(userId)) {
            return "用户订单不存在";
        }
        List<Order> list = this.lambdaQuery().eq(Order::getUserId, userId).list();
        list.forEach(order -> {
            orderItemService.lambdaQuery().eq(OrderItem::getOrderId, order.getOrderId()).list().forEach(orderItem -> {
                orderItemService.deleteOrderItemById(orderItem.getOrderItemId());
            });
        });

        this.lambdaUpdate().eq(Order::getUserId, userId).remove();
        return null;
    }

    @Override
    public String deleteOrderById(Integer orderId) {
        if (!existsOrderById(orderId)) {
            return "订单不存在";
        }
        orderItemService.lambdaQuery().eq(OrderItem::getOrderId, orderId).list().forEach(orderItem -> {
            orderItemService.deleteOrderItemById(orderItem.getOrderItemId());
        });
        this.removeById(orderId);
        return null;
    }

    @Override
    public String saveOrder(OrderDTO dto) {
        if (dto == null) {
            return "保存订单失败";
        }
        Order order = new Order();
        BeanUtils.copyProperties(dto, order);
        order.setUpdateTime(new Date());
        order.setTotalPrice(new BigDecimal(0));
        this.save(order);
        return null;
    }

    @Override
    public String updateOrder(OrderDTO dto) {
        System.out.println(dto);
        final Order order = this.lambdaQuery().eq(Order::getOrderId, dto.getOrderId()).one();
        if (order == null) {
            return "订单不存在";
        }
        BeanUtils.copyProperties(dto, order);
        order.setUpdateTime(new Date());
        return this.updateById(order) ? null : "更新订单失败";
    }

    public boolean existsOrdersByUserId(Integer userId) {
        return this.baseMapper.exists(Wrappers.<Order>lambdaQuery().eq(Order::getUserId, userId));
    }

    public boolean existsOrderById(Integer id) {
        return this.baseMapper.exists(Wrappers.<Order>lambdaQuery().eq(Order::getOrderId, id));
    }

}
