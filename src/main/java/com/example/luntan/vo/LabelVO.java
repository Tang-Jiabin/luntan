package com.example.luntan.vo;

import lombok.Data;

import java.time.Instant;

@Data
public class LabelVO {
    private Integer id;
    private String name;
    private String ioc;
    private Instant ctime;
}
