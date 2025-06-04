package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.admin.dto.DeliveryAddressDTO;
import com.example.entity.admin.vo.DeliveryAddressVO;
import com.example.entity.pojo.DeliveryAddress;
import com.example.mapper.DeliveryAddressMapper;
import com.example.service.DeliveryAddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeliveryAddressServiceImpl extends ServiceImpl<DeliveryAddressMapper, DeliveryAddress> implements DeliveryAddressService {

    @Override
    public List<DeliveryAddressVO> getDeliveryAddressByUserId(Integer userId) {
        List<DeliveryAddressVO> voList = new ArrayList<>();
        List<DeliveryAddress> deliveryAddresses = this.lambdaQuery().eq(DeliveryAddress::getUserId, userId).list();

        deliveryAddresses.forEach(deliveryAddress -> {
            DeliveryAddressVO deliveryAddressVO = new DeliveryAddressVO();
            BeanUtils.copyProperties(deliveryAddress, deliveryAddressVO);
            voList.add(deliveryAddressVO);
        });

        return voList;
    }

    @Override
    public DeliveryAddressVO getDeliveryAddressById(Integer id) {
        DeliveryAddress deliveryAddress = this.getById(id);
        DeliveryAddressVO deliveryAddressVO = new DeliveryAddressVO();
        BeanUtils.copyProperties(deliveryAddress, deliveryAddressVO);
        return deliveryAddressVO;
    }

    @Override
    public String updateDeliveryAddress(DeliveryAddressDTO deliveryAddressDTO) {
        Integer id = deliveryAddressDTO.getId();
        DeliveryAddress deliveryAddress = this.getById(id);
        BeanUtils.copyProperties(deliveryAddressDTO, deliveryAddress);
        return this.updateById(deliveryAddress) ? null : "修改失败";
    }

    @Override
    public String saveDeliveryAddress(DeliveryAddressDTO deliveryAddressDTO) {
        DeliveryAddress deliveryAddress = new DeliveryAddress();
        BeanUtils.copyProperties(deliveryAddressDTO, deliveryAddress);
        return this.save(deliveryAddress) ? null : "添加失败";
    }

    @Override
    public String deleteDeliveryAddressById(Integer id) {
        return this.removeById(id) ? null : "删除失败";
    }
}
