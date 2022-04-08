package com.example.luntan.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 自定义异常APIException
     */
    @ExceptionHandler(APIException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestResponse<Object> apiExceptionHandler(APIException e) {
        log.warn("异常:{}:{}", e.getCode(), e.getMsg());
        return RestResponse.error(e.getCode(), e.getMsg());
    }
}
