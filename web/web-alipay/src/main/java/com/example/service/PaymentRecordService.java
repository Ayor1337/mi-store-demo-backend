package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.pojo.PaymentRecord;

public interface PaymentRecordService extends IService<PaymentRecord> {


    String createPaymentRecord(PaymentRecord paymentRecord);
}
