package com.example.controller;

import com.example.entity.app.vo.CartItemVO;
import com.example.result.Result;
import com.example.service.CartService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    @Resource
    private CartService cartService;

    @GetMapping("/get")
    public Result<List<CartItemVO>> getCheckoutsByUserId(HttpServletRequest request) {
        Integer userId = Integer.parseInt(request.getAttribute("id").toString());
        return Result.dataMessageHandler(() -> cartService.getCheckoutsByUserId(userId), "获取购物车失败");
    }
}
