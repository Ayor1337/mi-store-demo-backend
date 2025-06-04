package com.example.entity.message;

import com.example.entity.admin.dto.OrderDTO;
import lombok.Data;

import java.util.List;

@Data
public class SubmitOrderMessage {
    private List<Integer> cartItemIds;
    private OrderDTO orderDTO;

}
