package com.example.controller.exception;

import com.example.result.Result;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class ValidationController {

    @ExceptionHandler(ValidationException.class)
    public Result<Void> validateException(ValidationException exception) {
        log.warn("Resolve [{} : {}]", exception.getClass(), exception.getMessage());
        return Result.fail(401, exception.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> constraintViolationException(ConstraintViolationException exception) {
        log.warn("Resolve [{} : {}]", exception.getClass(), exception.getMessage());
        return Result.fail(401, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> methodArgumentNotValidExceptionException(MethodArgumentNotValidException exception) {
        log.warn("Resolve [{} : {}]", exception.getClass(), exception.getMessage());
        return Result.fail(401, Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage());
    }
}