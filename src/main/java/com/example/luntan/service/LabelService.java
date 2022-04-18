package com.example.luntan.service;

import com.example.luntan.pojo.Label;
import com.example.luntan.vo.LabelVO;
import com.example.luntan.vo.PageVO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LabelService {
    List<LabelVO> findAll();

    Page<Label> findPage(Integer page, Integer limit, String title);

    PageVO<LabelVO> page2VO(Page<Label> labelPage);

    List<LabelVO> list2vo(List<Label> content);

    void del(Integer id);
}
