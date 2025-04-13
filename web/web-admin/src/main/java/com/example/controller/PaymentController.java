package com.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@Tag(name = "支付管理", description = "与支付相关的操作")
public class PaymentController {

    @GetMapping("/list")
    @Operation(summary = "获取支付记录", description = "获取所有支付记录")
    public String getPayments() {
        return "支付记录";
    }

    @PostMapping("/save_payment")
    @Operation(summary = "保存支付记录", description = "创建新的支付记录")
    public String savePayment(@RequestBody @Parameter(description = "支付详情") String paymentDetails) {
        return "支付记录保存成功";
    }
}