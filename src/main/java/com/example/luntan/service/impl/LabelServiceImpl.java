package com.example.luntan.service.impl;


import com.example.luntan.dao.LabelRepository;
import com.example.luntan.pojo.Label;
import com.example.luntan.service.LabelService;
import com.example.luntan.vo.LabelVO;
import com.example.luntan.vo.PageVO;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
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

    @Override
    public Page<Label> findPage(Integer page, Integer limit, String title) {

        page = page <= 1 ? 0 : page - 1;
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.DESC, "ctime");
        return labelRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();

            if (StringUtils.hasText(title)) {
                list.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + title + "%"));
            }

            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);
    }

    @Override
    public PageVO<LabelVO> page2VO(Page<Label> labelPage) {
        PageVO<LabelVO> pageVO = new PageVO<LabelVO>();
        BeanUtils.copyProperties(labelPage, pageVO);
        return pageVO;
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

    @Override
    public void del(Integer id) {
        labelRepository.deleteById(id);
    }
}
