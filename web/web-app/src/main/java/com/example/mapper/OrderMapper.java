package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.app.vo.OrderVO;
import com.example.entity.mapperResult.CommodityInOrderItem;
import com.example.entity.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper extends BaseMapper<Order> {

    List<CommodityInOrderItem> getCommodityInOrderItemByCartId(@Param("cartId") Integer cartId);

    List<OrderVO> getOrderVOByUserId(@Param("userId") Integer userId);
}
