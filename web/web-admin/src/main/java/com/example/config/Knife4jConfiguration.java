package com.example.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfiguration {

    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("all")
                .displayName("所有接口")
                .packagesToScan("com.example")
                .build();
    }
}
