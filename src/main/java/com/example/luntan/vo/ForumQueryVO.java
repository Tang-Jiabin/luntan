package com.example.luntan.vo;

import lombok.Data;

import java.util.List;

@Data
public class ForumQueryVO {

    private String title;
    private Integer lx;
    private Integer page;
    private Integer limit;
    private Integer uid;
    private Integer loginId;
    private List<Integer> ids;
}
