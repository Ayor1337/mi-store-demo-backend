package com.example.entity.app.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayConfirmVO {

    private Integer orderId;

    private String receiveAddress;

    private List<String> commodityNames;

    private BigDecimal totalPrice;
}
