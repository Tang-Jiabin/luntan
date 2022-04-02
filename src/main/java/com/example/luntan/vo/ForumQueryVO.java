package com.example.luntan.vo;

import lombok.Data;

@Data
public class ForumQueryVO {

    private String title;
    private Integer lx;
    private Integer page;
    private Integer limit;
    private Integer uid;
}
