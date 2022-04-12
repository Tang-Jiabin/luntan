package com.example.luntan.service;

import com.example.luntan.common.DataModelTypeEnum;

public interface DataModelService {
    void add(Integer uid, Integer id, DataModelTypeEnum typeEnum);
}
