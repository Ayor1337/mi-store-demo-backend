package com.example.controller;

import com.example.entity.admin.dto.DeliveryAddressDTO;
import com.example.entity.admin.vo.DeliveryAddressVO;
import com.example.result.Result;
import com.example.service.DeliveryAddressService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {

    @Resource
    private DeliveryAddressService deliveryAddressService;

    @GetMapping("/get")
    public Result<List<DeliveryAddressVO>> getDeliveryAddressByUserId(HttpServletRequest request) {
        Integer userId = Integer.parseInt(request.getAttribute("id").toString());
        return Result.dataMessageHandler(() -> deliveryAddressService.getDeliveryAddressByUserId(userId), "获取收货地址失败");
    }

    @GetMapping("/getById")
    public Result<DeliveryAddressVO> getDeliveryAddressById(HttpServletRequest request,
                                                            @RequestParam("id") Integer id) {
        Integer userId = Integer.parseInt(request.getAttribute("id").toString());
        return Result.dataMessageHandler(() -> deliveryAddressService.getDeliveryAddressById(id, userId), "获取收货地址失败");
    }

    @PostMapping("/update")
    public Result<Void> updateDeliveryAddress(HttpServletRequest request,
                                              @RequestBody DeliveryAddressDTO deliveryAddressDTO) {
        Integer userId = Integer.parseInt(request.getAttribute("id").toString());
        return Result.messageHandler(() -> deliveryAddressService.updateDeliveryAddress(deliveryAddressDTO, userId));
    }

    @PostMapping("/save")
    public Result<Void> saveDeliveryAddress(HttpServletRequest request,
                                            @RequestBody DeliveryAddressDTO deliveryAddressDTO) {
        Integer userId = Integer.parseInt(request.getAttribute("id").toString());
        deliveryAddressDTO.setUserId(userId);
        return Result.messageHandler(() -> deliveryAddressService.saveDeliveryAddress(deliveryAddressDTO, userId));
    }

    @PostMapping("/delete")
    public Result<Void> deleteDeliveryAddressById(HttpServletRequest request,
                                                  @RequestParam("id") Integer id) {
        Integer userId = Integer.parseInt(request.getAttribute("id").toString());
        return Result.messageHandler(() -> deliveryAddressService.deleteDeliveryAddressById(id, userId));
    }
}
