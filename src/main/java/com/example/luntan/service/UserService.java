package com.example.luntan.service;

import com.example.luntan.dto.UserDTO;
import com.example.luntan.pojo.User;
import com.example.luntan.vo.PageVO;
import com.example.luntan.vo.UserVO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    void login(UserDTO userDTO);

    UserDTO wxLogin(UserDTO userDTO);

    UserDTO update(UserDTO userDTO);

    List<UserDTO> findAllByIdList(List<Integer> uidList);

    UserDTO findById(Integer uid);

    Integer findCount();

    Page<User> findPage(Integer page, Integer limit, String nickname);

    PageVO<UserVO> page2VO(Page<User> userPage);

    List<UserVO> list2VO(List<User> content);

    void del(Integer id);
}
