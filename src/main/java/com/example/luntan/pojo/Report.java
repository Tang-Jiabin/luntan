package com.example.luntan.pojo;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Table(name = "luntan_report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer fid;

    private Integer uid;

    private String content;

    private Instant ctime;

    private Integer state;

    public String getCtime() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());
        if (this.ctime==null){
            ctime = Instant.now();
        }
        return dateFormat.format(this.ctime);
    }
}
