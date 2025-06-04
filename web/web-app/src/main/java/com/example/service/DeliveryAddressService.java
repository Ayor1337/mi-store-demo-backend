package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.admin.dto.DeliveryAddressDTO;
import com.example.entity.admin.vo.DeliveryAddressVO;
import com.example.entity.pojo.DeliveryAddress;

import java.util.List;

public interface DeliveryAddressService extends IService<DeliveryAddress> {
    List<DeliveryAddressVO> getDeliveryAddressByUserId(Integer userId);

    DeliveryAddressVO getDeliveryAddressById(Integer id, Integer userId);

    String updateDeliveryAddress(DeliveryAddressDTO deliveryAddressDTO, Integer userId);

    String saveDeliveryAddress(DeliveryAddressDTO deliveryAddressDTO, Integer userId);

    String deleteDeliveryAddressById(Integer id, Integer userId);
}
