package com.example.entity.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppSubmitOrderMessage {
    private Integer userId;
    private Integer addressId;
}
