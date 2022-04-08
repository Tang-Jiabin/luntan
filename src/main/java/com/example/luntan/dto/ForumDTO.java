package com.example.luntan.dto;

import com.example.luntan.pojo.Forum;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class ForumDTO extends Forum {

    private String ctime;
    private String utime;
}
