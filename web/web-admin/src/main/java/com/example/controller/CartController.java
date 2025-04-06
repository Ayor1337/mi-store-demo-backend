package com.example.controller;

import com.example.entity.vo.CartItemVO;
import com.example.entity.vo.CartVO;
import com.example.result.Result;
import com.example.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/get_items/{id}")
    public Result<List<CartItemVO>> getCartByUserId(@PathVariable Integer id) {
        CartVO cartVOByUserId = cartService.getCartVOByUserId(id);
        return Result.ok(cartVOByUserId.getCartItems());

    }
}
