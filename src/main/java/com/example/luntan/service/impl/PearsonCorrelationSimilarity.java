package com.example.luntan.service.impl;

import com.example.luntan.dto.BaseMode;
import com.example.luntan.dto.Distances;
import com.example.luntan.pojo.DataModel;
import com.example.luntan.service.Similarity;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PearsonCorrelationSimilarity implements Similarity {


    private final List<DataModel> dataModelList;

    public PearsonCorrelationSimilarity(List<DataModel> dataModelList) {
        this.dataModelList = dataModelList;
    }

    @Override
    public List<Distances> getDistances(Integer id, Integer type) {
        List<Distances> disList = new ArrayList<>();
        //构建数据模型
        List<BaseMode> modeList = type == 1 ? BaseMode.generateUserMode(this.dataModelList) : BaseMode.generateItemMode(this.dataModelList);
        BaseMode recommendMode = null;
        for (BaseMode mode : modeList) {
            if (mode.getId().equals(id)) {
                recommendMode = mode;
                break;
            }
        }
        if (recommendMode == null) {
            recommendMode = type == 1 ? BaseMode.generateDefaultUser(id, this.dataModelList) : BaseMode.generateDefaultItem(id, this.dataModelList);
            modeList.add(recommendMode);
        }
        //计算距离
        for (BaseMode mode : modeList) {
            if (!mode.getId().equals(id)) {
                double distance = pearsonDis(mode.getModeList(), recommendMode.getModeList());
                disList.add(new Distances(mode.getId(), distance));
//                log.info("基类：{}", recommendMode);
//                log.info("对比：{}", mode);
//                log.info("距离：{}", distance);
            }
        }
        return disList;
    }

    private double pearsonDis(List<BaseMode> modeList, List<BaseMode> recommendModeList) {
        int n = 0;
        double sum_x2 = 0, sum_y2 = 0;
        double sum_xy = 0, sum_x = 0, sum_y = 0;
        for (BaseMode mode : modeList) {
            for (BaseMode recommendMode : recommendModeList) {
                if (mode.getId().equals(recommendMode.getId())) {
                    n++;
                    sum_x += mode.getScore();
                    sum_y += recommendMode.getScore();
                    sum_x2 += Math.pow(mode.getScore(), 2);
                    sum_y2 += Math.pow(recommendMode.getScore(), 2);
                    sum_xy += mode.getScore() * recommendMode.getScore();
                }
            }
        }
        double numerator = sum_xy - sum_x * sum_y / n;
        double denominator = Math.sqrt((sum_x2 - Math.pow(sum_x, 2) / n)) * Math.sqrt((sum_y2 - Math.pow(sum_y, 2) / n));
        return numerator / denominator;
    }


}
