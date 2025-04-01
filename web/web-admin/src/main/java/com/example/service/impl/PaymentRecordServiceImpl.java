package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.PaymentRecord;
import com.example.mapper.PaymentRecordMapper;
import com.example.service.PaymentRecordService;
import org.springframework.stereotype.Service;

@Service
public class PaymentRecordServiceImpl extends ServiceImpl<PaymentRecordMapper, PaymentRecord> implements PaymentRecordService {
}
