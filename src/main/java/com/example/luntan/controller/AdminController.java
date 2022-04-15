package com.example.luntan.controller;

import com.example.luntan.common.RestResponse;
import com.example.luntan.dto.AdminDTO;
import com.example.luntan.dto.PushOpenidDTO;
import com.example.luntan.dto.UserDTO;
import com.example.luntan.vo.UserVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("admin")
public class AdminController {

    @ApiOperation("登录")
    @ApiImplicitParam(name = "PushOpenidDTO", value = "登录", required = true, dataTypeClass = AdminDTO.class, paramType = "body")
    @PostMapping(value = "/login")
    public RestResponse<AdminDTO> login(@RequestBody AdminDTO adminDTO) {
        return RestResponse.success(adminDTO);
    }
}
