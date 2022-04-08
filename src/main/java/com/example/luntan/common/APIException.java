package com.example.luntan.common;

import lombok.Getter;

@Getter
public class APIException extends RuntimeException {
    private int code;
    private String msg;

    public APIException() {
        this(500, "系统错误");
    }

    public APIException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

