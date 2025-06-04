package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.app.vo.OrderItemVO;
import com.example.entity.mapperResult.CommodityInOrderItem;
import com.example.entity.pojo.Commodity;
import com.example.entity.pojo.OrderItem;
import com.example.mapper.OrderItemMapper;
import com.example.mapper.OrderMapper;
import com.example.service.CommodityService;
import com.example.service.OrderItemService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private CommodityService commodityService;

    @Override
    public boolean saveOrderItemsByOrderId(Integer orderId, Integer cartId) {
        List<OrderItem> orderItems = new ArrayList<>();
        List<CommodityInOrderItem> commodityInOrderItems =
                orderMapper.getCommodityInOrderItemByCartId(cartId);
        commodityInOrderItems.forEach(commodityInOrderItem -> {
            OrderItem orderItem = new OrderItem(null, orderId, commodityInOrderItem.getCommodityId(),
                    commodityInOrderItem.getQuantity(), commodityInOrderItem.getPrice());
            orderItems.add(orderItem);
        });

        return this.saveBatch(orderItems);
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {
        return this.lambdaQuery().eq(OrderItem::getOrderId, orderId).list();
    }

    @Override
    public List<OrderItemVO> getOrderItemVOByOrderId(Integer orderId) {
        List<OrderItem> orderItems = this.getOrderItemsByOrderId(orderId);
        List<OrderItemVO> orderItemVOS = new ArrayList<>();
        orderItems.forEach(orderItem -> {
            OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyProperties(orderItem, orderItemVO);
            Commodity commodity = commodityService.getById(orderItem.getCommodityId());
            orderItemVO.setImageUrl(commodity.getImages().split(",")[0]);
            orderItemVO.setName(commodity.getFullName());
            orderItemVOS.add(orderItemVO);
        });

        return orderItemVOS;
    }

}
