package com.example.controller;

import com.example.entity.app.vo.CartItemVO;
import com.example.entity.message.AddCartItemMessage;
import com.example.entity.message.ChangeCartItemMessage;
import com.example.entity.message.DeleteCartItemMessage;
import com.example.entity.message.result.AddCartItemResult;
import com.example.entity.message.result.ChangeCartItemResult;
import com.example.entity.message.result.DeleteCartItemResult;
import com.example.result.Result;
import com.example.service.AccountService;
import com.example.service.CartItemService;
import com.example.service.CartService;
import com.example.util.JWTUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Resource
    private CartService cartService;

    @Resource
    private CartItemService cartItemService;

    @Resource
    private AccountService accountService;

    private final RabbitTemplate rpcTemplate;

    public CartController(@Qualifier("rpcRabbitTemplate") RabbitTemplate rpcTemplate) {
        this.rpcTemplate = rpcTemplate;
    }

    @Resource
    private JWTUtil jwtUtil;

    @GetMapping("/get_cartItems")
    public Result<List<CartItemVO>> getCartItems(HttpServletRequest request) {
        Integer userId = Integer.parseInt(request.getAttribute("id").toString());
        return Result.dataMessageHandler(() -> cartService.getCartByUserId(userId), "获取购物车失败");
    }

    @PostMapping("/add")
    public Result<Void> addCartItem(@RequestParam("commodityId") Integer commodityId,
                                    @RequestParam("quantity") Integer quantity,
                                    HttpServletRequest request) {
        Integer userId = Integer.parseInt(request.getAttribute("id").toString());
        AddCartItemResult reply = (AddCartItemResult) rpcTemplate.convertSendAndReceive("cart", "add_cartItem", new AddCartItemMessage(userId, commodityId, quantity));
        if (reply != null) {
            if (reply.getSuccess())
                return Result.ok();
            else
                return Result.fail(402, reply.getMessage());
        }
        return Result.fail();
    }

    @PostMapping("/delete")
    public Result<Void> deleteCartItem(@RequestParam("cartItemId") Integer cartItemId,
                                       HttpServletRequest request) {
        Integer userId = Integer.parseInt(request.getAttribute("id").toString());
        Integer cartId = cartService.getCartIdByUserId(userId);
        Integer quantity = cartItemService.getQuantityById(cartItemId);
        Integer commodityId = cartItemService.getCommodityIdById(cartItemId);
        DeleteCartItemResult reply =
                (DeleteCartItemResult) rpcTemplate.convertSendAndReceive("cart", "delete_cartItem", new DeleteCartItemMessage(cartItemId, cartId, quantity, commodityId));
        if (reply != null) {
            if (reply.getSuccess())
                return Result.ok();
            else
                return Result.fail(402, reply.getMessage());
        }
        return Result.fail();
    }

    @PostMapping("/update")
    public Result<Void> changeCartItemQuantity(@RequestParam("cartItemId") Integer cartItemId,
                                               @RequestParam("quantity") Integer quantity,
                                               HttpServletRequest request) {
        int userId = Integer.parseInt(request.getAttribute("id").toString());
        Integer cartId = cartService.getCartIdByUserId(userId);
        ChangeCartItemMessage changeCartItemMessage = new ChangeCartItemMessage(cartItemId, cartId, quantity);
        ChangeCartItemResult reply = (ChangeCartItemResult) rpcTemplate.convertSendAndReceive("cart", "update_cartItem", changeCartItemMessage);
        if (reply != null) {
            if (reply.getSuccess())
                return Result.ok();
            else
                return Result.fail(402, reply.getMessage());
        }
        return Result.fail();
    }

    @PostMapping("/submit")
    public Result<Void> submitCart(HttpServletRequest request) {
        Integer userId = Integer.parseInt(request.getAttribute("id").toString());
        return null;
    }

    @PostMapping("/checkin")
    public Result<Void> checkInCart(HttpServletRequest request,
                                    @RequestParam("cartItemId") Integer cartItemId) {
        Integer userId = Integer.parseInt(request.getAttribute("id").toString());
        return Result.messageHandler(() -> cartService.checkinById(cartItemId, userId));
    }

    @PostMapping("/checkout")
    public Result<Void> checkOutCart(HttpServletRequest request,
                                     @RequestParam("cartItemId") Integer cartItemId) {
        Integer userId = Integer.parseInt(request.getAttribute("id").toString());
        return Result.messageHandler(() -> cartService.checkoutById(cartItemId, userId));
    }
}
