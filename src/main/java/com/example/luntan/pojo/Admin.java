package com.example.luntan.pojo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "luntan_admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String pwd;

    private String nickname;

}
