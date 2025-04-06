package com.example.controller.exception;

import com.example.result.Result;
import com.example.result.ResultCodeEnum;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ValidationController {

    @ExceptionHandler(ValidationException.class)
    public Result<Void> validateException(ValidationException exception) {
        log.warn("Resolve [{} : {}]", exception.getClass(), exception.getMessage());
        return Result.build(null, ResultCodeEnum.VALIDATE_FAILED);
    }
}