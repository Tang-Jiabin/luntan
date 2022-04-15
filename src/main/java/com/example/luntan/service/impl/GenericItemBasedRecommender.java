package com.example.luntan.service.impl;

import com.example.luntan.dto.Distances;
import com.example.luntan.pojo.DataModel;
import com.example.luntan.service.Recommend;
import com.example.luntan.service.Similarity;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GenericItemBasedRecommender implements Recommend {

    private final List<DataModel> dataModelList;
    private final Similarity similarity;

    public GenericItemBasedRecommender(List<DataModel> dataModelList, Similarity similarity) {
        this.similarity = similarity;
        this.dataModelList = dataModelList;
    }

    @Override
    public List<Integer> recommendedBecause(Integer id, Integer page, Integer limit) {
        List<Distances> distances = similarity.getDistances(id, 2);
        List<Distances> sortList = distances.stream().filter(dis -> {
            return dis.getDis() > 0;
        }).sorted(Comparator.comparing(Distances::getDis).reversed()).collect(Collectors.toList());
        Set<Integer> recommendFidSet = new HashSet<>();
        for (Distances itemDis : sortList) {
            for (DataModel dataModel : this.dataModelList) {
                if (itemDis.getId().equals(dataModel.getFid()) && !itemDis.getId().equals(id)) {
                    recommendFidSet.add(dataModel.getFid());
                }
            }
        }
        return recommendFidSet.stream().skip((long) (page - 1) * limit).limit(limit).collect(Collectors.toList());
    }
}
