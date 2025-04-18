package com.example.event;

import lombok.Getter;

@Getter
public class OrderPriceChangeEvent {

    private final Integer orderId;

    public OrderPriceChangeEvent(Integer orderId) {
        this.orderId = orderId;
    }

}
