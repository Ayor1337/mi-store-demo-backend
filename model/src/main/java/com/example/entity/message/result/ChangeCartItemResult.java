package com.example.entity.message.result;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeCartItemResult {

    private Boolean success;

    private String message;

}
