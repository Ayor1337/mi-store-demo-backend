package com.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.OrderDTO;
import com.example.entity.pojo.Order;
import com.example.entity.vo.OrderVO;
import com.example.mapper.OrderMapper;
import com.example.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {


    @Override
    public List<OrderVO> getAllOrders() {
        List<OrderVO> voList = new ArrayList<>();
        this.list().forEach(order -> {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);
            voList.add(orderVO);
        });
        return voList;
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
            voList.add(orderVO);
        });
        return voList;
    }

    @Override
    public String deleteOrdersByUserId(Integer userId) {
        if (!existsOrdersByUserId(userId)) {
            return "用户订单不存在";
        }
        this.lambdaUpdate().eq(Order::getUserId, userId).remove();
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
        this.save(order);
        return null;
    }

    public boolean existsOrdersByUserId(Integer userId) {
        return this.baseMapper.exists(Wrappers.<Order>lambdaQuery().eq(Order::getUserId, userId));
    }

}
