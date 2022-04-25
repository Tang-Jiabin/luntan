package com.example.luntan.service;

import com.example.luntan.dto.UserDTO;
import com.example.luntan.pojo.Pl;
import com.example.luntan.vo.PageVO;
import com.example.luntan.vo.PlAddVO;
import com.example.luntan.vo.PlQueryVO;
import com.example.luntan.vo.PlVO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PlService {
    void add(PlAddVO plAddVO);

    Page<Pl> findPage(PlQueryVO plQueryVO);

    PageVO<PlVO> page2vo(Page<Pl> plPage);

    List<PlVO> plAddUserInfo(List<PlVO> content, List<UserDTO> userDTOList);

    Integer findCount();

    void del(Integer id, Integer uid);
}
