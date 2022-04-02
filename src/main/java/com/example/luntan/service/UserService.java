package com.example.luntan.service;

import com.example.luntan.dto.UserDTO;

public interface UserService {
    void login(UserDTO userDTO);

    UserDTO wxLogin(UserDTO userDTO);

    UserDTO update(UserDTO userDTO);
}
