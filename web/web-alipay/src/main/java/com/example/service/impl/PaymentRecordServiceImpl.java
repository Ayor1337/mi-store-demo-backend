package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.pojo.PaymentRecord;
import com.example.mapper.PaymentRecordMapper;
import com.example.service.PaymentRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaymentRecordServiceImpl extends ServiceImpl<PaymentRecordMapper, PaymentRecord> implements PaymentRecordService {

    @Override
    public String createPaymentRecord(PaymentRecord paymentRecord) {
        // 插入支付记录
        this.save(paymentRecord);
        return null;


    }

}
