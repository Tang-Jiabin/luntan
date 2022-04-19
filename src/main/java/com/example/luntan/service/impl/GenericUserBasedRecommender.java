package com.example.luntan.service.impl;

import com.example.luntan.dto.Distances;
import com.example.luntan.pojo.DataModel;
import com.example.luntan.service.Recommend;
import com.example.luntan.service.Similarity;

import java.util.*;
import java.util.stream.Collectors;

public class GenericUserBasedRecommender implements Recommend {

    private final List<DataModel> dataModelList;
    private final Similarity similarity;

    public GenericUserBasedRecommender(List<DataModel> dataModelList, Similarity similarity) {
        this.similarity = similarity;
        this.dataModelList = dataModelList;
    }

    @Override
    public List<Integer> recommendedBecause(Integer id, Integer page, Integer limit) {
        List<Distances> distances = similarity.getDistances(id, 1);
        List<Distances> sortList = distances.stream().filter(dis -> {
            return dis.getDis() > 0;
        }).sorted(Comparator.comparing(Distances::getDis).reversed()).collect(Collectors.toList());
        Set<Integer> recommendFidSet = new HashSet<>();
        Set<Integer> userFidSet = new HashSet<>();
        for (Distances userDis : sortList) {
            for (DataModel dataModel : this.dataModelList) {
                if (userDis.getId().equals(dataModel.getUid())) {
                    recommendFidSet.add(dataModel.getFid());
                }
                if (dataModel.getUid().equals(id)) {
                    userFidSet.add(dataModel.getFid());
                }
            }
        }
        recommendFidSet.removeAll(userFidSet);
//        return recommendFidSet.stream().skip((long) (page - 1) * limit).limit(limit).collect(Collectors.toList());
        return new ArrayList<>(recommendFidSet);
    }
}
