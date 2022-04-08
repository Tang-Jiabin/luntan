package com.example.luntan.vo;

import com.example.luntan.pojo.Pl;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;



@Data
public class PlVO  {

    private Integer id;

    private Integer pid;

    private Integer fid;

    private Integer uid;

    private String storey;

    private String content;

    private String ctime;

    private String logo;

    private String uname;
}
