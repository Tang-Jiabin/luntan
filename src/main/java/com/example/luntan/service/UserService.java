package com.example.luntan.service;

import com.example.luntan.dto.UserDTO;
import com.example.luntan.pojo.User;

import java.util.List;

public interface UserService {
    void login(UserDTO userDTO);

    UserDTO wxLogin(UserDTO userDTO);

    UserDTO update(UserDTO userDTO);

    List<UserDTO> findAllByIdList(List<Integer> uidList);
}
