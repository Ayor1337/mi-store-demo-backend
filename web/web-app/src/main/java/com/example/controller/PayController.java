package com.example.controller;

import com.example.entity.app.vo.PayConfirmVO;
import com.example.entity.message.AppSubmitOrderMessage;
import com.example.entity.message.result.SubmitOrderResult;
import com.example.result.Result;
import com.example.service.OrderService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/pay")
public class PayController {

    @Resource
    private OrderService orderService;

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;


    private final RabbitTemplate rpcTemplate;


    public PayController(@Qualifier("rpcRabbitTemplate") RabbitTemplate rpcTemplate) {
        this.rpcTemplate = rpcTemplate;
    }

    @PostMapping("submit")
    public Result<Integer> submitCartAndCreateOrder(HttpServletRequest request,
                                                    @RequestParam("addressId") Integer addressId) {
        Integer userId = Integer.parseInt(request.getAttribute("id").toString());
        AppSubmitOrderMessage appSubmitOrderMessage = new AppSubmitOrderMessage(userId, addressId);
        SubmitOrderResult reply = (SubmitOrderResult) rpcTemplate.convertSendAndReceive("cart", "submit_cart", appSubmitOrderMessage);
        if (reply != null) {
            if (reply.getSuccess())
                return Result.ok(reply.getOrderId());
            else
                return Result.fail(402, reply.getMessage());
        }
        return Result.fail();
    }

    @GetMapping("/confirm_info")
    public Result<PayConfirmVO> confirmInfo(HttpServletRequest request,
                                            @RequestParam("orderId") Integer orderId) {
        Integer userId = Integer.parseInt(request.getAttribute("id").toString());
        return Result.dataMessageHandler(() -> orderService.getPayConfirmVO(orderId, userId), "获取支付信息失败");
    }

    @GetMapping("/order_deadline")
    public Result<Date> orderDeadline(@RequestParam("orderId") Integer orderId) {
        String key = "pendingOrder:" + orderId;
        // 以毫秒为单位获取剩余过期时间
        Long remainMs = redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
        if (remainMs == null) {
            return Result.fail(402, "查询失败");
        }
        if (remainMs < 0) {
            // -2: key 不存在；-1: key 不设置过期
            return Result.fail(402, "订单不存在或未设置过期时间");
        }
        // 当前时间 + 剩余毫秒数 = 绝对过期时间
        Date deadline = new Date(System.currentTimeMillis() + remainMs);
        return Result.ok(deadline);
    }
}
