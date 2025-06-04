package com.example.entity.message.result;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubmitOrderResult {

    private Boolean success;

    private String message;

    private Integer orderId;
}
