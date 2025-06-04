package com.example.controller;

import com.example.entity.app.vo.OrderItemVO;
import com.example.entity.app.vo.OrderVO;
import com.example.result.Result;
import com.example.service.OrderItemService;
import com.example.service.OrderService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @Resource
    private OrderItemService orderItemService;

    @GetMapping("/get")
    public Result<List<OrderVO>> getOrderVOByUserId(HttpServletRequest request) {
        Integer userId = Integer.parseInt(request.getAttribute("id").toString());
        return Result.dataMessageHandler(() -> orderService.getOrderVOByUserId(userId), "获取订单失败");
    }

    @GetMapping("/get/items")
    public Result<List<OrderItemVO>> getOrderItemVOByOrderId(HttpServletRequest request,
                                                             @RequestParam("orderId") Integer orderId) {
        Integer userId = Integer.parseInt(request.getAttribute("id").toString());
        if (orderService.confirmOrderOwnerByUserId(orderId, userId)) {
            return Result.dataMessageHandler(() -> orderItemService.getOrderItemVOByOrderId(orderId), "获取订单详情失败");
        }
        return Result.fail();
    }
}
