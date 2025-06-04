package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.admin.dto.DeliveryAddressDTO;
import com.example.entity.admin.vo.DeliveryAddressVO;
import com.example.entity.pojo.DeliveryAddress;
import com.example.mapper.DeliveryAddressMapper;
import com.example.service.AccountService;
import com.example.service.DeliveryAddressService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DeliveryAddressServiceImpl extends ServiceImpl<DeliveryAddressMapper, DeliveryAddress> implements DeliveryAddressService {


    @Resource
    private AccountService accountService;

    @Override
    public List<DeliveryAddressVO> getDeliveryAddressByUserId(Integer userId) {
        List<DeliveryAddressVO> voList = new ArrayList<>();
        List<DeliveryAddress> deliveryAddresses =
                this.lambdaQuery()
                        .eq(DeliveryAddress::getUserId, userId)
                        .eq(DeliveryAddress::getIsDeleted, false)
                        .list();

        deliveryAddresses.forEach(deliveryAddress -> {
            DeliveryAddressVO deliveryAddressVO = new DeliveryAddressVO();
            BeanUtils.copyProperties(deliveryAddress, deliveryAddressVO);
            voList.add(deliveryAddressVO);
        });
        return voList;
    }

    @Override
    public DeliveryAddressVO getDeliveryAddressById(Integer id, Integer userId) {
        DeliveryAddress deliveryAddress = this.lambdaQuery()
                .eq(DeliveryAddress::getId, id)
                .eq(DeliveryAddress::getUserId, userId)
                .one();
        DeliveryAddressVO deliveryAddressVO = new DeliveryAddressVO();
        BeanUtils.copyProperties(deliveryAddress, deliveryAddressVO);
        return deliveryAddressVO;
    }

    @Override
    public String updateDeliveryAddress(DeliveryAddressDTO deliveryAddressDTO, Integer userId) {
        Integer id = deliveryAddressDTO.getId();
        DeliveryAddress deliveryAddress =
                this.lambdaQuery()
                        .eq(DeliveryAddress::getId, id)
                        .eq(DeliveryAddress::getUserId, userId)
                        .one();
        if (deliveryAddress == null) {
            return "该地址不存在";
        }
        BeanUtils.copyProperties(deliveryAddressDTO, deliveryAddress);
        return this.updateById(deliveryAddress) ? null : "修改失败";
    }

    @Override
    public String saveDeliveryAddress(DeliveryAddressDTO deliveryAddressDTO, Integer userId) {
        if (!accountService.existUserById(userId)) {
            return "该用户不存在";
        }
        DeliveryAddress deliveryAddress = new DeliveryAddress();
        deliveryAddress.setIsDeleted(false);
        BeanUtils.copyProperties(deliveryAddressDTO, deliveryAddress);
        return this.save(deliveryAddress) ? null : "添加失败";
    }

    @Override
    public String deleteDeliveryAddressById(Integer id, Integer userId) {
        DeliveryAddress address = this.lambdaQuery().eq(DeliveryAddress::getId, id).eq(DeliveryAddress::getUserId, userId).one();
        address.setIsDeleted(true);
        return this.updateById(address) ? null : "删除失败";
    }

}
