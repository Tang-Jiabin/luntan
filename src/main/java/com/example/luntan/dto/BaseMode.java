package com.example.luntan.dto;

import com.example.luntan.pojo.DataModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseMode {
    private Integer id;
    private Integer score;
    private List<BaseMode> modeList = new ArrayList<>();

    public static List<BaseMode> generateItemMode(List<DataModel> dataModelList) {
        List<BaseMode> itemModeList = new ArrayList<>();
        for (DataModel dataModel : dataModelList) {
            BaseMode itemMode = new BaseMode();
            boolean exist = false;
            for (BaseMode im : itemModeList) {
                if (im.getId().equals(dataModel.getFid())) {
                    itemMode = im;
                    exist = true;
                    break;
                }
            }
            itemMode.setId(dataModel.getFid());
            List<BaseMode> userList = itemMode.getModeList();
            BaseMode userMode = new BaseMode(dataModel.getUid(), dataModel.getScore(), null);
            if (!userList.contains(userMode)) {
                userList.add(userMode);
            }
            if (!exist) {
                itemModeList.add(itemMode);
            }
        }
        return itemModeList;
    }

    public static BaseMode generateDefaultItem(Integer fid, List<DataModel> dataModelList) {
        BaseMode itemMode = new BaseMode();
        fid = fid == null ? 0 : fid;
        itemMode.setId(fid);
        //取所有用户平均值
        List<BaseMode> list = new ArrayList<>();
        Map<Integer, List<DataModel>> fidGroup = dataModelList.stream().collect(Collectors.groupingBy(DataModel::getUid));
        for (Map.Entry<Integer, List<DataModel>> entry : fidGroup.entrySet()) {
            Integer id = entry.getKey();
            Integer score = entry.getValue().stream().map(DataModel::getScore).collect(Collectors.averagingInt(Integer::intValue)).intValue();
            BaseMode userMode = new BaseMode(id, score, null);
            list.add(userMode);
        }
        itemMode.setModeList(list);
        return itemMode;
    }

    public static List<BaseMode> generateUserMode(List<DataModel> dataModelList) {
        List<BaseMode> userModeList = new ArrayList<>();
        for (DataModel dataModel : dataModelList) {
            BaseMode userMode = new BaseMode();
            boolean exist = false;
            for (BaseMode um : userModeList) {
                if (um.getId().equals(dataModel.getUid())) {
                    userMode = um;
                    exist = true;
                    break;
                }
            }
            userMode.setId(dataModel.getUid());
            List<BaseMode> item = userMode.getModeList();
            BaseMode itemMode = new BaseMode(dataModel.getFid(), dataModel.getScore(), null);
            if (!item.contains(itemMode)) {
                item.add(itemMode);
            }
            if (!exist) {
                userModeList.add(userMode);
            }
        }
        return userModeList;
    }

    public static BaseMode generateDefaultUser(Integer uid, List<DataModel> dataModelList) {
        BaseMode userMode = new BaseMode();
        uid = uid == null ? 0 : uid;
        userMode.setId(uid);
        //取所有用户平均值
        List<BaseMode> list = new ArrayList<>();
        Map<Integer, List<DataModel>> fidGroup = dataModelList.stream().collect(Collectors.groupingBy(DataModel::getFid));
        for (Map.Entry<Integer, List<DataModel>> entry : fidGroup.entrySet()) {
            Integer fid = entry.getKey();
            Integer score = entry.getValue().stream().map(DataModel::getScore).collect(Collectors.averagingInt(Integer::intValue)).intValue();
            BaseMode itemMode = new BaseMode(fid, score, null);
            list.add(itemMode);
        }
        userMode.setModeList(list);
        return userMode;
    }
}
