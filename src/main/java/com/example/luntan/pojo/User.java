package com.example.luntan.pojo;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Table(name = "luntan_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String openid;

    private String sessionKey;

    private String name;

    private String sign;

    private String logo;

    private Integer sex;

    private Instant ctime;

    public String getCtime() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());
        if (this.ctime != null) {
            return dateFormat.format(this.ctime);
        }
        return "";
    }

    public String getSex() {
        return this.sex == null || this.sex == 0 ? "男" : "女";
    }
}
