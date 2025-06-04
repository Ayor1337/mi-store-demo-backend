package com.example;

import com.example.mapper.OrderItemMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import static com.example.util.DataEncoder.getAnonymousPhone;

public class ExampleTest {

    @Resource
    private OrderItemMapper orderItemMapper;

    @Test
    public void contextLoads() {
        System.out.println(getAnonymousPhone("18171366558"));

    }


}
