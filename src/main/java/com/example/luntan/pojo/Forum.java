package com.example.luntan.pojo;

import lombok.Data;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Table(name = "luntan_forum")
public class Forum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer uid;
    private Integer labelsid;
    private Instant ctime;
    private Instant utime = Instant.now();
    private String content;
    private String picture;
    private Integer dzmun = 0;
    private Integer plmun = 0;
    private Integer scmun = 0;

    public String getCtime(){
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());
        return dateFormat.format(this.ctime);
    }

    public String getUtime(){
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());
        return dateFormat.format(this.utime);
    }

}
