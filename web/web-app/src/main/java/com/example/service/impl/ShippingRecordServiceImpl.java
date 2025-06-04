package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.pojo.ShippingRecord;
import com.example.mapper.ShippingRecordMapper;
import com.example.service.ShippingRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ShippingRecordServiceImpl extends ServiceImpl<ShippingRecordMapper, ShippingRecord> implements ShippingRecordService {
}
