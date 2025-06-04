package com.example.controller;

import com.example.entity.admin.dto.DeliveryAddressDTO;
import com.example.entity.admin.vo.DeliveryAddressVO;
import com.example.result.Result;
import com.example.service.DeliveryAddressService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery")
public class DeliveryAddressController {

    @Resource
    private DeliveryAddressService deliveryAddressService;

    @GetMapping("/list/{userId}")
    public Result<List<DeliveryAddressVO>> getDeliveryAddressesByUserId(@PathVariable("userId") Integer userId) {
        return Result.dataMessageHandler(() -> deliveryAddressService.getDeliveryAddressByUserId(userId), "获取收货地址失败");
    }

    @GetMapping("/get/{id}")
    public Result<DeliveryAddressVO> getDeliveryAddressById(@PathVariable("id") Integer id) {
        return Result.dataMessageHandler(() -> deliveryAddressService.getDeliveryAddressById(id), "获取收货地址失败");
    }

    @PostMapping("/update")
    public Result<Void> updateDeliveryAddress(@RequestBody DeliveryAddressDTO deliveryAddressDTO) {
        return Result.messageHandler(() -> deliveryAddressService.updateDeliveryAddress(deliveryAddressDTO));
    }

    @PostMapping("/save")
    public Result<Void> saveDeliveryAddress(@RequestBody DeliveryAddressDTO deliveryAddressDTO) {
        return Result.messageHandler(() -> deliveryAddressService.saveDeliveryAddress(deliveryAddressDTO));
    }

    @PostMapping("/delete")
    public Result<Void> deleteDeliveryAddress(@RequestParam("id") Integer id) {
        return Result.messageHandler(() -> deliveryAddressService.deleteDeliveryAddressById(id));
    }
}
