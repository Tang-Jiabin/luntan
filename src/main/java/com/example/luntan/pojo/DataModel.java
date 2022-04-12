package com.example.luntan.pojo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "luntan_data_model")
public class DataModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer uid;

    private Integer fid;

    private Integer score;

    private Boolean ck;
    private Boolean dz;
    private Boolean pl;
    private Boolean sc;


}
