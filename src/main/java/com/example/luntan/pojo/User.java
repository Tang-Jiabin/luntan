package com.example.luntan.pojo;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "l_user")
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

}
