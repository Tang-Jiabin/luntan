package com.example.luntan.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageVO<T> {
    private Integer page;
    private Integer totalPages;
    private Long totalElements;
    private List<T> content;
}
