package com.example.luntan.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.luntan.common.WechatConfig;
import com.example.luntan.dto.PushOpenidDTO;
import com.example.luntan.dto.UserDTO;
import com.example.luntan.service.UserService;
import com.example.luntan.service.WechatService;
import com.example.luntan.util.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class WechatServiceImpl implements WechatService {

    private final UserService userService;

    public WechatServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDTO getOpenid(String code) {

        //填写你小程序的appid 和 secret ,  还有前端传给你的code ,最后一个参数是固定的
        String tokenUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=" + WechatConfig.APP_ID + "&secret=" + WechatConfig.SECRET + "&js_code=" + code + "&grant_type=authorization_code";
        JSONObject accessToken = OkHttpUtil.get(tokenUrl);

        Integer resCode = accessToken.getInteger("code");
        UserDTO userDTO = new UserDTO();
        if (resCode.equals(200)) {
            String openid = accessToken.getString("openid");
            String sessionKey = accessToken.getString("session_key");
            userDTO.setOpenid(openid);
            userDTO.setSessionKey(sessionKey);
        }


        return userDTO;

    }

    @Override
    public UserDTO wxLogin(PushOpenidDTO pushOpenidDTO) {
        UserDTO userDTO = getOpenid(pushOpenidDTO.getCode());
        BeanUtils.copyProperties(pushOpenidDTO,userDTO);
        return userService.wxLogin(userDTO);
    }


}
