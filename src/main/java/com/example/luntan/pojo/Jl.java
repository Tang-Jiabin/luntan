package com.example.luntan.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "luntan_jl")
@AllArgsConstructor
@NoArgsConstructor
public class Jl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer fid;

    private Integer uid;

    private Instant ctime;
}
