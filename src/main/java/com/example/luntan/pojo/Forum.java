package com.example.luntan.pojo;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

@Data
@Entity
@Table(name = "luntan_forum")
public class Forum implements Comparator<Forum> {

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

    private Double score;

    public String getCtime() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());
        return dateFormat.format(this.ctime);
    }

    public Long getCtimeEpochSecond() {
        return this.ctime.getEpochSecond();
    }

    public String getUtime() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());
        return dateFormat.format(this.utime);
    }

    @Override
    public int compare(Forum o1, Forum o2) {
//        score=log5z+t/86400
        long epochSecond = LocalDateTime.of(2022, 4, 1, 0, 0, 0).toEpochSecond(ZoneOffset.of("+08:00"));
        double score = Math.log(o1.dzmun + o1.plmun + o1.scmun) / Math.log(5) + o1.getCtimeEpochSecond() - epochSecond / 86400d;
        double score2 = Math.log(o2.dzmun + o2.plmun + o2.scmun) / Math.log(5) + o2.getCtimeEpochSecond() - epochSecond / 86400d;
        return Double.compare(score2, score);
    }




}
