package com.example.luntan.service;

import com.example.luntan.dto.ForumDTO;
import com.example.luntan.dto.UserDTO;
import com.example.luntan.pojo.Forum;
import com.example.luntan.pojo.Jl;
import com.example.luntan.pojo.Sc;
import com.example.luntan.vo.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ForumService {
    void add(ForumDTO forumDTO);

    Page<Forum> findPage(ForumQueryVO forumQueryVO);

    void sc(Integer uid, Integer id);

    void dz(Integer uid, Integer id);

    ForumDTO findById(Integer id);


    ForumVO dto2vo(ForumDTO forumDTO, UserDTO userDTO, Integer loginId);

    PageVO<ForumVO> page2VO(Page<Forum> page);

    List<ForumVO> forumList2VO(List<Forum> content, List<UserDTO> userDTOList, Integer loginId);

    UserForumVO findUserDataStatistics(Integer uid);

    List<Sc> findScList(Integer uid);

    void addJl(ItemIdVO itemIdVO);

    List<Jl> findJlList(Integer loginId);

    Integer findCount();

    void del(Integer id);
}
