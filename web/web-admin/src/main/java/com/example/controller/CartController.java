package com.example.controller;

import com.example.entity.dto.CartItemDTO;
import com.example.entity.vo.CartItemVO;
import com.example.entity.vo.CartVO;
import com.example.result.Result;
import com.example.service.CartItemService;
import com.example.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;

    @GetMapping("/get_items/{id}")
    public Result<List<CartItemVO>> getCartByUserId(@PathVariable Integer id) {
        CartVO cartVOByUserId = cartService.getCartVOByUserId(id);
        if (cartVOByUserId == null) {
            return Result.fail();
        }
        return Result.ok(cartVOByUserId.getCartItems());

    }

    @PostMapping("/save_items")
    public Result<Void> saveCartItem(@RequestBody CartItemDTO dto) {
        return Result.messageHandler(() -> cartItemService.saveCartItem(dto));
    }

    @PostMapping("/delete/{cartItemId}")
    public Result<Void> deleteCartItemById(@PathVariable Integer cartItemId) {
        return Result.messageHandler(() -> cartItemService.deleteCartItemById(cartItemId));
    }

    @PostMapping("/update")
    public Result<Void> updateCartItemById(@RequestBody CartItemDTO dto) {
        return Result.messageHandler(() -> cartItemService.updateCartItemById(dto));
    }

    @PostMapping("/increase_quantity")
    public Result<Void> increaseCartItemQuantity(@RequestParam Integer cartItemId, @RequestParam Integer quantity) {
        return Result.messageHandler(() -> cartItemService.increaseCartItemQuantity(cartItemId, quantity));
    }

    @PostMapping("/decrease_quantity")
    public Result<Void> decreaseCartItemQuantity(@RequestParam Integer cartItemId, @RequestParam Integer quantity) {
        return Result.messageHandler(() -> cartItemService.decreaseCartItemQuantity(cartItemId, quantity));
    }
}
