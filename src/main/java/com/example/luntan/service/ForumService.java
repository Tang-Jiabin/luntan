package com.example.luntan.service;

import com.example.luntan.dto.ForumDTO;
import com.example.luntan.vo.ForumQueryVO;
import com.example.luntan.vo.ForumVO;
import com.example.luntan.vo.PageVO;

public interface ForumService {
    void add(ForumDTO forumDTO);

    PageVO<ForumVO> findPage(ForumQueryVO forumQueryVO);

    void sc(Integer uid, Integer id);

    void dz(Integer uid, Integer id);
}
