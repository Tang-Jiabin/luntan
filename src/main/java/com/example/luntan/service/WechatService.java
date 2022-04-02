package com.example.luntan.service;

import com.example.luntan.dto.PushOpenidDTO;
import com.example.luntan.dto.UserDTO;

public interface WechatService {
    UserDTO getOpenid(String code);

    UserDTO wxLogin(PushOpenidDTO pushOpenidDTO);
}
