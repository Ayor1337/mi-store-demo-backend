package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.OrderItemDTO;
import com.example.entity.pojo.Commodity;
import com.example.entity.pojo.OrderItem;
import com.example.entity.vo.OrderItemVO;
import com.example.event.OrderPriceChangeEvent;
import com.example.mapper.OrderItemMapper;
import com.example.service.CommodityService;
import com.example.service.OrderItemService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {


    @Resource
    private CommodityService commodityService;

    private final ApplicationEventPublisher publisher;

    @Autowired
    public OrderItemServiceImpl(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public List<OrderItemVO> getOrderItemVOsByOrderId(Integer orderId) {
        if (orderId == null) {
            return null;
        }
        List<OrderItemVO> orderItemVOList = new ArrayList<>();
        publisher.publishEvent(new OrderPriceChangeEvent(orderId));
        this.lambdaQuery().eq(OrderItem::getOrderId, orderId).list().forEach(orderItem -> {
            OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyProperties(orderItem, orderItemVO);
            Optional.ofNullable(commodityService.getById(orderItem.getCommodityId())).ifPresent(commodity -> orderItemVO.setCommodityName(commodity.getFullName()));
            orderItemVOList.add(orderItemVO);
        });

        return orderItemVOList;

    }

    @Override
    public String deleteOrderItemById(Integer orderItemId) {
        if (orderItemId == null) {
            return "订单详情ID为空";
        }
        OrderItem orderItem = this.getById(orderItemId);
        String message = commodityService.increaseCommodityStock(orderItem.getCommodityId(), orderItem.getQuantity());
        if (message != null) {
            return message;
        }
        if (this.removeById(orderItemId)) {
            publisher.publishEvent(new OrderPriceChangeEvent(orderItem.getOrderId()));
            return null;
        } else {
            return "删除订单详情失败";
        }
    }

    @Override
    public String saveOrderItem(OrderItemDTO orderItemDTO) {
        if (orderItemDTO == null) {
            return "订单详情信息为空";
        }
        OrderItem orderItem = new OrderItem();
        Commodity commodity = commodityService.getById(orderItemDTO.getCommodityId());
        if (commodity == null || commodity.getStock() < orderItemDTO.getQuantity()) {
            return "商品不存在或库存不足";
        }
        BeanUtils.copyProperties(orderItemDTO, orderItem);
        orderItem.setPrice(commodity.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
        publisher.publishEvent(new OrderPriceChangeEvent(orderItem.getOrderId()));
        return this.save(orderItem) ? null : "保存订单详情失败";
    }

    @Override
    public String increaseOrderItemQuantity(Integer orderItemId, Integer quantity) {
        if (orderItemId == null || quantity == null) {
            return "订单详情ID或购买数量为空";
        }
        OrderItem orderItem = this.getById(orderItemId);
        if (orderItem == null) {
            return "订单详情不存在";
        }
        String messageFormDecreaseCommodityStock = commodityService.decreaseCommodityStock(orderItem.getCommodityId(), quantity);
        if (messageFormDecreaseCommodityStock != null) {
            return messageFormDecreaseCommodityStock;
        }
        orderItem.setQuantity(orderItem.getQuantity() + quantity);
        orderItem.setPrice(orderItem.getPrice().multiply(new BigDecimal(quantity)));
        publisher.publishEvent(new OrderPriceChangeEvent(orderItem.getOrderId()));
        return this.updateById(orderItem) ? null : "更新订单详情失败";
    }

    @Override
    public String decreaseOrderItemQuantity(Integer orderItemId, Integer quantity) {
        if (orderItemId == null || quantity == null) {
            return "订单详情ID或购买数量为空";
        }
        OrderItem orderItem = this.getById(orderItemId);
        if (orderItem == null) {
            return "订单详情不存在";
        }
        commodityService.increaseCommodityStock(orderItem.getCommodityId(), quantity);
        orderItem.setQuantity(orderItem.getQuantity() - quantity);
        if (orderItem.getQuantity() <= 0) {
            this.removeById(orderItemId);
            return "订单记录小于或等于0，自动移出订单";
        }
        orderItem.setPrice(orderItem.getPrice().multiply(new BigDecimal(quantity)));
        publisher.publishEvent(new OrderPriceChangeEvent(orderItem.getOrderId()));
        this.updateById(orderItem);
        return null;
    }

    @Override
    public String changeOrderItemQuantity(Integer orderItemId, Integer quantity) {
        if (orderItemId == null || quantity == null) {
            return "订单详情ID或购买数量为空";
        }
        OrderItem orderItem = this.getById(orderItemId);
        if (orderItem == null) {
            return "订单详情不存在";
        }
        Integer oldQuantity = orderItem.getQuantity();
        orderItem.setQuantity(quantity);
        orderItem.setPrice(orderItem.getPrice().multiply(new BigDecimal(quantity)));
        publisher.publishEvent(new OrderPriceChangeEvent(orderItem.getOrderId()));
        if (this.updateById(orderItem)) {
            commodityService.changeCommodityStock(orderItem.getCommodityId(), quantity - oldQuantity);
            return null;
        } else {
            return "更新订单详情失败";
        }
    }

}
