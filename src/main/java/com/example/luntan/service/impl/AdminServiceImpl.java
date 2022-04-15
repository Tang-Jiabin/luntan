package com.example.luntan.service.impl;

import com.example.luntan.common.APIException;
import com.example.luntan.dao.AdminRepository;
import com.example.luntan.dao.DzRepository;
import com.example.luntan.dto.AdminDTO;
import com.example.luntan.pojo.Admin;
import com.example.luntan.service.AdminService;
import com.example.luntan.service.ForumService;
import com.example.luntan.service.PlService;
import com.example.luntan.service.UserService;
import com.example.luntan.vo.AdminVO;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final ForumService forumService;
    private final UserService userService;
    private final DzRepository dzRepository;
    private final PlService plService;

    @Override
    public AdminVO login(AdminDTO adminDTO) {

        Optional<Admin> adminOptional = adminRepository.findByNameAndPwd(adminDTO.getName(),adminDTO.getPwd());
        if (adminOptional.isEmpty()) {
            throw new APIException(400,"用户名或密码不正确");
        }
        Admin admin = adminOptional.get();
        AdminVO adminVO = new AdminVO();
        BeanUtils.copyProperties(admin,adminVO);
        return adminVO;
    }

    @Override
    public Map statistics(AdminDTO adminDTO) {
        //帖子
        Integer tieCount = forumService.findCount();
        //用户
        Integer userCount = userService.findCount();
        //点赞
        Integer dzCount = dzRepository.findCount();
        //评论
        Integer plCount = plService.findCount();

        Map<String,Integer> map = new HashMap<>();
        map.put("tieCount",tieCount);
        map.put("userCount",userCount);
        map.put("dzCount",dzCount);
        map.put("plCount",plCount);
        return map;
    }
}
