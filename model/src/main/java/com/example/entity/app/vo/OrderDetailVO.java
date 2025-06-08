package com.example.entity.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OrderDetailVO {
    private Integer orderId;
    private String name;
    private String address;
    private String phone;
    private List<OrderItemVO> orderItems;
}
