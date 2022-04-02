package com.example.luntan.controller;


import com.example.luntan.common.RestResponse;
import com.example.luntan.dto.PushOpenidDTO;
import com.example.luntan.dto.UserDTO;
import com.example.luntan.service.UserService;
import com.example.luntan.service.WechatService;
import com.example.luntan.vo.UserVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@Slf4j
@RestController
@RequestMapping("User")
public class UserController {

    private final WechatService wechatService;
    private final UserService userService;

    public UserController(WechatService wechatService, UserService userService) {
        this.wechatService = wechatService;
        this.userService = userService;
    }

    @ApiOperation("微信登录")
    @ApiImplicitParam(name = "PushOpenidDTO", value = "微信登录", required = true, dataTypeClass = PushOpenidDTO.class, paramType = "body")
    @PostMapping(value = "/getOpenid")
    public RestResponse<UserVO> getOpenid(@RequestBody PushOpenidDTO pushOpenidDTO) {
        UserDTO userDTO = wechatService.wxLogin(pushOpenidDTO);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userDTO, userVO);
        return RestResponse.success(userVO);
    }

    @ApiOperation("更新")
    @ApiImplicitParam(name = "UserDTO", value = "更新用户信息", required = true, dataTypeClass = UserDTO.class, paramType = "body")
    @PostMapping(value = "/update")
    public RestResponse<UserVO> update(@RequestBody UserDTO userDTO) throws FileNotFoundException {

        userDTO = userService.update(userDTO);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userDTO, userVO);
        return RestResponse.success(userVO);
    }
}
