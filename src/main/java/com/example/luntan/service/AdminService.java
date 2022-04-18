package com.example.luntan.service;

import com.example.luntan.dto.AdminDTO;
import com.example.luntan.vo.AdminVO;

import java.util.Map;

public interface AdminService {
    AdminVO login(AdminDTO adminDTO);

    Map statistics(AdminDTO adminDTO);

    void update(String name, String oldPwd, String pwd);
}
