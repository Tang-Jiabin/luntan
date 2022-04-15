package com.example.luntan.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdminForumVO extends ForumQueryVO{
    private Integer id;

    private String name;

    private String pwd;

    private String nickname;
}
