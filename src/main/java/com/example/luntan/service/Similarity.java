package com.example.luntan.service;

import com.example.luntan.dto.Distances;

import java.util.List;

public interface Similarity {

    List<Distances> getDistances(Integer uid);
}
