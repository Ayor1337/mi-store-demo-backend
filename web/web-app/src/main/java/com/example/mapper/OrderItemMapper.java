package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.pojo.OrderItem;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrderItemMapper extends BaseMapper<OrderItem> {

    @Select("select * from OrderItems where order_id = ${orderId}")
    List<OrderItem> getOrderItemsByOrderId(Integer orderId);

    @Select("select commodity_id from OrderItems where order_id = ${orderId}")
    List<Integer> getCommodityIdsByOrderId(Integer orderId);
}
