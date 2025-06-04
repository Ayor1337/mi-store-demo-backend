package com.example.entity.message.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteCartItemResult implements Serializable {

    private Boolean success;

    private String message;


}
