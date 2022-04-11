package com.example.luntan.service.impl;


import com.example.luntan.dao.LabelRepository;
import com.example.luntan.pojo.Label;
import com.example.luntan.service.LabelService;
import com.example.luntan.vo.LabelVO;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;

    @Override
    public List<LabelVO> findAll() {
        List<Label> labelList = labelRepository.findAll();
        return list2vo(labelList);
    }

    public List<LabelVO> list2vo(List<Label> labelList) {
        List<LabelVO> labelVOList = new ArrayList<>();
        for (Label label : labelList) {
            LabelVO labelVO = new LabelVO();
            BeanUtils.copyProperties(label, labelVO);
            labelVOList.add(labelVO);
        }
        return labelVOList;
    }
}
