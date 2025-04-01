package com.example;

import com.auth0.jwt.algorithms.Algorithm;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.example.service.AccountService;
import com.example.util.JWTUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootTest
public class ExampleTest {

    @Resource
    private AccountService accountService;



    @Value("${spring.datasource.url}")
    private String url;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Test
    public void test() {
        System.out.println(passwordEncoder.encode("admin"));
    }

    @Test
    public void createDTO(){
        FastAutoGenerator.create("url", "username", "password")
                // 全局配置
                .globalConfig((scanner, builder) -> builder.author("ayor"))
                // 包配置
                .packageConfig((scanner, builder) -> builder.parent(scanner.apply("请输入包名？")))
                // 策略配置
                .strategyConfig((scanner, builder) -> builder.addInclude(getTables(scanner.apply("请输入表名，多个英文逗号分隔？所有输入 all")))
                        .entityBuilder()
                        .enableLombok()
                        .addTableFills(
                                new Column("create_time", FieldFill.INSERT)
                        )
                        .build())
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }

    protected static List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }
}
