package com.example.controller;

import com.alipay.api.AlipayApiException;
import com.example.entity.entity.PayOrder;
import com.example.service.OrderService;
import com.example.template.AlipayTemplate;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alipay")
@CrossOrigin
public class AlipayController {

    @Resource
    private AlipayTemplate alipayTemplate;

    @Resource
    private OrderService orderService;

    @GetMapping(value = "/pay", produces = "text/html")
    @ResponseBody
    public String pay(@RequestParam Integer orderId) throws AlipayApiException {
        PayOrder payOrder = orderService.createPayOrder(orderId);
        if (payOrder == null) {
            return "订单已过期";
        }
        return alipayTemplate.pay(payOrder);
    }

    @PostMapping("/notify")  // 注意这里必须是POST接口
    public String payNotify(HttpServletRequest request) {

        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            orderService.changePaymentStatus(request.getParameterMap());
        }

        return "success";
    }
}
