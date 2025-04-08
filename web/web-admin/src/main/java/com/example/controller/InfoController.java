package com.example.controller;

import com.example.result.Result;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/info")
public class InfoController {

    @RequestMapping("/get_name")
    public Result<String> getLoginUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Result.ok();
    }
}
