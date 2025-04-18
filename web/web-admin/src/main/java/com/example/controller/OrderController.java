package com.example.controller;

import com.example.entity.dto.OrderDTO;
import com.example.entity.dto.OrderItemDTO;
import com.example.entity.vo.OrderItemVO;
import com.example.entity.vo.OrderVO;
import com.example.result.Result;
import com.example.service.OrderItemService;
import com.example.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@Tag(name = "订单管理", description = "订单相关的接口")
public class OrderController {

    @Resource
    private OrderService orderService;

    @Resource
    private OrderItemService orderItemService;

    @GetMapping("/list")
    @Operation(summary = "获取订单列表", description = "获取所有订单的详细信息")
    public Result<List<OrderVO>> list() {
        return Result.dataMessageHandler(() -> orderService.getAllOrders(), "订单不存在");
    }

    @GetMapping("/get_by_id/{id}")
    @Operation(summary = "获取订单详情", description = "根据订单ID获取订单的详细信息")
    public Result<OrderVO> getOrderById(@PathVariable @Parameter(description = "订单ID") Integer id) {
        return Result.dataMessageHandler(() -> orderService.getOrderById(id), "订单不存在");
    }

    @PostMapping("/save")
    @Operation(summary = "保存订单", description = "保存订单信息")
    public Result<Void> saveOrder(@RequestBody @Parameter(description = "订单信息") OrderDTO orderDTO) {
        return Result.messageHandler(() -> orderService.saveOrder(orderDTO));
    }

    @PostMapping("/detail/save")
    @Operation(summary = "保存订单详情", description = "保存订单详情信息")
    public Result<Void> saveOrderDetail(@RequestBody @Parameter(description = "订单详情信息") OrderItemDTO orderItemDTO) {
        return Result.messageHandler(() -> orderItemService.saveOrderItem(orderItemDTO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新订单", description = "更新订单信息")
    public Result<Void> updateOrder(@RequestBody @Parameter(description = "订单信息") OrderDTO orderDTO) {
        return Result.messageHandler(() -> orderService.updateOrder(orderDTO));
    }

    @GetMapping("/detail/{orderId}")
    @Operation(summary = "获取订单详情", description = "根据订单ID获取订单的详细信息")
    public Result<List<OrderItemVO>> getOrderDetail(@PathVariable @Parameter(description = "订单ID") Integer orderId) {
        return Result.dataMessageHandler(() -> orderItemService.getOrderItemVOsByOrderId(orderId), "订单不存在");
    }

    @DeleteMapping("/detail/delete/{orderItemId}")
    @Operation(summary = "删除订单详情", description = "根据详细订单ID删除订单详情")
    public Result<Void> deleteOrderDetail(@PathVariable @Parameter(description = "详细订单ID") Integer orderItemId) {
        return Result.messageHandler(() -> orderItemService.deleteOrderItemById(orderItemId));
    }

    @DeleteMapping("/delete/{orderId}")
    @Operation(summary = "删除订单", description = "根据订单ID删除")
    public Result<Void> deleteOrderById(@PathVariable @Parameter(description = "购物车ID") Integer orderId) {
        return Result.messageHandler(() -> orderService.deleteOrderById(orderId));
    }

    @DeleteMapping("/delete_by_cart/{cartId}")
    @Operation(summary = "删除订单", description = "根据购物车ID删除订单")
    public Result<Void> deleteOrderByCartId(@PathVariable @Parameter(description = "购物车ID") Integer cartId) {
        return Result.messageHandler(() -> orderService.deleteOrdersByUserId(cartId));
    }

    @PostMapping("/increase_quantity")
    @Operation(summary = "增加订单数量", description = "增加订单数量")
    public Result<Void> increaseOrderQuantity(@RequestParam @Parameter(description = "详细订单ID") Integer orderItemId,
                                              @RequestParam @Parameter(description = "数量") Integer quantity) {
        return Result.messageHandler(() -> orderItemService.increaseOrderItemQuantity(orderItemId, quantity));
    }

    @PostMapping("/decrease_quantity")
    @Operation(summary = "减少订单数量", description = "减少订单数量")
    public Result<Void> decreaseOrderQuantity(@RequestParam @Parameter(description = "详细订单ID") Integer orderItemId,
                                              @RequestParam @Parameter(description = "数量") Integer quantity) {
        return Result.messageHandler(() -> orderItemService.decreaseOrderItemQuantity(orderItemId, quantity));
    }

    @PostMapping("/change_quantity")
    @Operation(summary = "修改订单数量", description = "修改订单数量")
    public Result<Void> changeOrderQuantity(@RequestParam @Parameter(description = "详细订单ID") Integer orderItemId,
                                            @RequestParam @Parameter(description = "数量") Integer quantity) {
        return Result.messageHandler(() -> orderItemService.changeOrderItemQuantity(orderItemId, quantity));
    }


}
