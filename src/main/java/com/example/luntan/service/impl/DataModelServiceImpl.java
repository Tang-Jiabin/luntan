package com.example.luntan.service.impl;

import com.example.luntan.common.DataModelTypeEnum;
import com.example.luntan.dao.DataModelRepository;
import com.example.luntan.pojo.DataModel;
import com.example.luntan.service.DataModelService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class DataModelServiceImpl implements DataModelService {

    private final DataModelRepository dataModelRepository;

    @Async
    @Override
    public void add(Integer uid, Integer id, DataModelTypeEnum typeEnum) {
        Optional<DataModel> dataModelOptional = dataModelRepository.findByUidAndAndFid(uid, id);
        dataModelOptional.ifPresentOrElse(dataModel -> {
            typeSwitch(typeEnum, dataModel);
        }, () -> {
            DataModel dataModel = new DataModel();
            dataModel.setUid(uid);
            dataModel.setFid(id);
            dataModel.setScore(0);
            dataModel.setCk(false);
            dataModel.setDz(false);
            dataModel.setPl(false);
            dataModel.setSc(false);
            typeSwitch(typeEnum, dataModel);
        });
    }

    private void typeSwitch(DataModelTypeEnum typeEnum, DataModel dataModel) {
        switch (typeEnum) {
            case CK:
                if (!dataModel.getCk()) {
                    dataModel.setCk(true);
                    dataModel.setScore(dataModel.getScore() + DataModelTypeEnum.CK.getScore());
                }
                break;
            case DZ:
                if (!dataModel.getDz()) {
                    dataModel.setDz(true);
                    dataModel.setScore(dataModel.getScore() + DataModelTypeEnum.DZ.getScore());
                }
                break;
            case PL:
                if (!dataModel.getPl()) {
                    dataModel.setPl(true);
                    dataModel.setScore(dataModel.getScore() + DataModelTypeEnum.PL.getScore());
                }
                break;
            case SC:
                if (!dataModel.getSc()) {
                    dataModel.setSc(true);
                    dataModel.setScore(dataModel.getScore() + DataModelTypeEnum.SC.getScore());
                }
                break;
        }
        dataModelRepository.save(dataModel);
    }
}
