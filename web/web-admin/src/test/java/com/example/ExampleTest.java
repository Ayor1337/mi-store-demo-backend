package com.example;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class ExampleTest {

    @Resource
    PasswordEncoder encoder;

    @Test
    public void test() {
        System.out.println(encoder.encode("admin"));
    }
}
