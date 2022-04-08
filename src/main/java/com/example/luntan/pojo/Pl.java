package com.example.luntan.pojo;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Table(name = "l_pl")
public class Pl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer pid;

    private Integer fid;

    private Integer uid;

    private Integer storey;

    private String content;

    private Instant ctime;

    public String getCtime(){
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());
        return dateFormat.format(this.ctime);
    }
}
