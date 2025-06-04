package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.admin.dto.DeliveryAddressDTO;
import com.example.entity.admin.vo.DeliveryAddressVO;
import com.example.entity.pojo.DeliveryAddress;

import java.util.List;

public interface DeliveryAddressService extends IService<DeliveryAddress> {
    List<DeliveryAddressVO> getDeliveryAddressByUserId(Integer userId);

    DeliveryAddressVO getDeliveryAddressById(Integer id);

    String updateDeliveryAddress(DeliveryAddressDTO deliveryAddressDTO);

    String saveDeliveryAddress(DeliveryAddressDTO deliveryAddressDTO);

    String deleteDeliveryAddressById(Integer id);
}
