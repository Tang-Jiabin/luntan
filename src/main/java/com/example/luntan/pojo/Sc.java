package com.example.luntan.pojo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "l_sc")
public class Sc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer uid;

    private Integer fid;
}
