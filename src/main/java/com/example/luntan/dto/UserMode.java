package com.example.luntan.dto;

import com.example.luntan.pojo.DataModel;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class UserMode {
    private Integer id;
    private List<ItemMode> itemModeList = new ArrayList<>();

    public static List<UserMode> generateUserMode(List<DataModel> dataModelList) {
        List<UserMode> userModeList = new ArrayList<>();
        for (DataModel dataModel : dataModelList) {
            UserMode userMode = new UserMode();
            boolean exist = false;
            for (UserMode um : userModeList) {
                if (um.getId().equals(dataModel.getUid())) {
                    userMode = um;
                    exist = true;
                    break;
                }
            }
            userMode.setId(dataModel.getUid());
            List<ItemMode> item = userMode.getItemModeList();
            ItemMode itemMode = new ItemMode(dataModel.getFid(), dataModel.getScore(), null);
            if (!item.contains(itemMode)) {
                item.add(itemMode);
            }
            if (!exist) {
                userModeList.add(userMode);
            }
        }
        return userModeList;
    }

    public static UserMode generateDefaultUser(Integer uid, List<DataModel> dataModelList) {
        UserMode userMode = new UserMode();
        userMode.setId(uid);
        //取所有用户平均值
        List<ItemMode> list = new ArrayList<>();
        Map<Integer, List<DataModel>> fidGroup = dataModelList.stream().collect(Collectors.groupingBy(DataModel::getFid));
        for (Map.Entry<Integer, List<DataModel>> entry : fidGroup.entrySet()) {
            Integer fid = entry.getKey();
            Integer score = entry.getValue().stream().map(DataModel::getScore).collect(Collectors.averagingInt(Integer::intValue)).intValue();
            ItemMode itemMode = new ItemMode(fid, score, null);
            list.add(itemMode);
        }
        userMode.setItemModeList(list);
        return userMode;
    }
}
