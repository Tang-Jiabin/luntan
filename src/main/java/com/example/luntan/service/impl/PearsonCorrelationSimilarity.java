package com.example.luntan.service.impl;

import com.example.luntan.dto.Distances;
import com.example.luntan.dto.ItemMode;
import com.example.luntan.dto.UserMode;
import com.example.luntan.pojo.DataModel;
import com.example.luntan.service.Similarity;
import lombok.extern.slf4j.Slf4j;


import java.util.*;

@Slf4j
public class PearsonCorrelationSimilarity implements Similarity {


    private final List<DataModel> dataModelList;

    public PearsonCorrelationSimilarity(List<DataModel> dataModelList) {
        this.dataModelList = dataModelList;
    }


    @Override
    public List<Distances> getDistances(Integer uid) {
        List<Distances> disList = new ArrayList<>();

        //生成基于用户的数据模型
        List<UserMode> userModeList = UserMode.generateUserMode(this.dataModelList);

        //获取推荐用户的数据
        UserMode recommendUser = null;
        for (UserMode userMode : userModeList) {
            if (userMode.getId().equals(uid)) {
                recommendUser = userMode;
                break;
            }
        }
        //如果推荐用户没有评分项，生成默认项
        if (recommendUser == null) {
            recommendUser = UserMode.generateDefaultUser(uid, this.dataModelList);
            userModeList.add(recommendUser);
        }

        //计算距离
        for (UserMode userMode : userModeList) {
            if (!userMode.getId().equals(uid)) {
                double distance = pearsonDis(userMode.getItemModeList(), recommendUser.getItemModeList());
                disList.add(new Distances(userMode.getId(), distance));
                log.info("比较用户：{}", userMode);
                log.info("推荐用户：{}", recommendUser);
                log.info("距离：{}", distance);
            }
        }

        return disList;
    }

    private double pearsonDis(List<ItemMode> item, List<ItemMode> recommendUserItem) {
        int n = 0;
        double sum_xy = 0, sum_x = 0, sum_y = 0;
        double sum_x2 = 0, sum_y2 = 0;
        for (ItemMode itemMode : item) {
            for (ItemMode userItemMode : recommendUserItem) {
                if (itemMode.getId().equals(userItemMode.getId())) {
                    n++;
                    sum_x += itemMode.getScore();
                    sum_y += userItemMode.getScore();
                    sum_x2 += Math.pow(itemMode.getScore(), 2);
                    sum_y2 += Math.pow(userItemMode.getScore(), 2);
                    sum_xy += itemMode.getScore() * userItemMode.getScore();
                }
            }
        }
        double numerator = sum_xy - sum_x * sum_y / n;
        double denominator = Math.sqrt((sum_x2 - Math.pow(sum_x, 2) / n)) * Math.sqrt((sum_y2 - Math.pow(sum_y, 2) / n));
        if (denominator <= 0) return 0.0;
        return numerator / denominator;
    }

}
