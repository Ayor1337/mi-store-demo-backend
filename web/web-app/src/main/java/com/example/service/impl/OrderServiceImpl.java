package com.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.app.vo.OrderVO;
import com.example.entity.app.vo.PayConfirmVO;
import com.example.entity.pojo.Commodity;
import com.example.entity.pojo.DeliveryAddress;
import com.example.entity.pojo.Order;
import com.example.entity.pojo.OrderItem;
import com.example.mapper.OrderItemMapper;
import com.example.mapper.OrderMapper;
import com.example.service.CommodityService;
import com.example.service.DeliveryAddressService;
import com.example.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.example.util.DataEncoder.getAnonymousPhone;

@Service
@Transactional
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    private OrderItemMapper orderItemMapper;

    @Resource
    private DeliveryAddressService deliveryAddressService;

    @Resource
    private CommodityService commodityService;


    @Override
    public Integer createNewOrder(Integer userId, Integer addressId) {
        // 创建空订单
        Order order = new Order(null, userId, new BigDecimal(0), null, null, addressId, null, null);
        this.save(order);
        return order.getOrderId();
    }

    @Override
    public boolean initOrder(Integer orderId) {
        Order order = this.getById(orderId);
        List<OrderItem> orderItems = orderItemMapper.getOrderItemsByOrderId(orderId);
        BigDecimal totalPrice = new BigDecimal("0");
        for (OrderItem orderItem : orderItems) {
            totalPrice = totalPrice.add(orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
        }
        order.setTotalPrice(totalPrice);

        return this.updateById(order);
    }

    @Override
    public List<OrderVO> getOrderVOByUserId(Integer userId) {
        return this.baseMapper.getOrderVOByUserId(userId);
    }

    @Override
    public boolean confirmOrderOwnerByUserId(Integer orderId, Integer userId) {
        return this.baseMapper.exists(Wrappers.<Order>lambdaQuery().eq(Order::getOrderId, orderId).eq(Order::getUserId, userId));
    }

    @Override
    public PayConfirmVO getPayConfirmVO(Integer orderId, Integer userId) {
        Order order = this.lambdaQuery()
                .eq(Order::getOrderId, orderId)
                .eq(Order::getUserId, userId)
                .one();
        PayConfirmVO payConfirmVO = new PayConfirmVO();
        payConfirmVO.setOrderId(order.getOrderId());

        DeliveryAddress deliveryAddress = deliveryAddressService.getById(order.getAddressId());
        String receiveAddress = deliveryAddress.getName() + " "
                + getAnonymousPhone(deliveryAddress.getPhone()) + " "
                + deliveryAddress.getAddress();
        payConfirmVO.setReceiveAddress(receiveAddress);
        List<Integer> commodityIds = orderItemMapper.getCommodityIdsByOrderId(orderId);
        List<Commodity> commodities = commodityService.listByIds(commodityIds);
        List<String> commodityNames = commodities.stream().map(Commodity::getFullName).toList();
        payConfirmVO.setCommodityNames(commodityNames);
        payConfirmVO.setTotalPrice(order.getTotalPrice());
        return payConfirmVO;
    }

}
