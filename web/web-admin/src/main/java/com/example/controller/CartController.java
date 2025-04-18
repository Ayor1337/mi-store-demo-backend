package com.example.controller;

import com.example.entity.dto.CartItemDTO;
import com.example.entity.dto.OrderDTO;
import com.example.entity.vo.CartItemVO;
import com.example.result.Result;
import com.example.service.CartItemService;
import com.example.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@Tag(name = "购物车管理", description = "与购物车相关的操作")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;

    @GetMapping("/get_items/{id}")
    @Operation(summary = "获取购物车商品", description = "根据用户ID获取购物车中的商品")
    public Result<List<CartItemVO>> getCartByUserId(@PathVariable @Parameter(description = "用户ID") Integer id) {
        return Result.dataMessageHandler(() -> cartService.getCartVOByUserId(id).getCartItems(), "获取购物车失败");
    }

    @GetMapping("/get_cartId/{id}")
    @Operation(summary = "获取购物车ID", description = "根据用户ID获取购物车ID")
    public Result<Integer> getCartIdByUserId(@PathVariable @Parameter(description = "用户ID") Integer id) {
        return Result.dataMessageHandler(() -> cartService.getCartVOByUserId(id).getCartId(), "获取购物车失败");
    }

    @PostMapping("/save_items")
    @Operation(summary = "保存购物车商品", description = "添加商品到购物车")
    public Result<Void> saveCartItem(@RequestBody @Valid @Parameter(description = "购物车商品详情") CartItemDTO dto) {
        return Result.messageHandler(() -> cartItemService.saveCartItem(dto));
    }

    @PostMapping("/delete/{cartItemId}")
    @Operation(summary = "删除购物车商品", description = "根据ID删除购物车中的商品")
    public Result<Void> deleteCartItemById(@PathVariable @Parameter(description = "购物车商品ID") Integer cartItemId) {
        return Result.messageHandler(() -> cartItemService.deleteCartItemById(cartItemId));
    }

    @DeleteMapping("/delete_batch")
    @Operation(summary = "批量删除", description = "批量删除购物车商品")
    public Result<Void> deleteCartItemByIds(@RequestParam("ids") @Parameter(description = "购物车商品ID列表") List<Integer> ids) {
        return Result.messageHandler(() -> cartItemService.batchDeleteByIds(ids));
    }

    @PostMapping("/update")
    @Operation(summary = "更新购物车商品", description = "更新购物车中的商品信息")
    public Result<Void> updateCartItemById(@RequestBody @Valid @Parameter(description = "更新后的购物车商品详情") CartItemDTO dto) {
        return Result.messageHandler(() -> cartItemService.updateCartItemById(dto));
    }

    @PostMapping("/increase_quantity")
    @Operation(summary = "增加商品数量", description = "增加购物车中商品的数量")
    public Result<Void> increaseCartItemQuantity(@RequestParam @Parameter(description = "购物车商品ID") Integer cartItemId, @RequestParam @Parameter(description = "增加的数量") Integer quantity) {
        return Result.messageHandler(() -> cartItemService.increaseCartItemQuantity(cartItemId, quantity));
    }

    @PostMapping("/decrease_quantity")
    @Operation(summary = "减少商品数量", description = "减少购物车中商品的数量")
    public Result<Void> decreaseCartItemQuantity(@RequestParam @Parameter(description = "购物车商品ID") Integer cartItemId, @RequestParam @Parameter(description = "减少的数量") Integer quantity) {
        return Result.messageHandler(() -> cartItemService.decreaseCartItemQuantity(cartItemId, quantity));
    }

    @PostMapping("/change_quantity")
    @Operation(summary = "修改商品数量", description = "修改购物车中商品的数量")
    public Result<Void> changeCartItemQuantity(@RequestParam @Parameter(description = "购物车商品ID") Integer cartItemId, @RequestParam @Parameter(description = "修改的数量") Integer quantity) {
        return Result.messageHandler(() -> cartItemService.changeCartItemQuantity(cartItemId, quantity));
    }

    @PostMapping("/submit_order")
    public Result<Void> submitOrder(@RequestBody @Parameter(description = "创建订单") OrderDTO orderDTO,
                                    @RequestParam(value = "cartItemsIds") @Parameter(description = "订单信息") List<Integer> cartItemsId) {
        return Result.messageHandler(() -> cartService.submitCart(orderDTO, cartItemsId));
    }
}