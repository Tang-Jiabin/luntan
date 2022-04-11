package com.example.luntan.controller;

import com.example.luntan.common.RestResponse;
import com.example.luntan.service.LabelService;
import com.example.luntan.vo.ForumQueryVO;
import com.example.luntan.vo.LabelVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("Labels")
@AllArgsConstructor
public class LabelsController {

    private final LabelService labelService;

    @ApiOperation("标签列表")
    @ApiImplicitParam(name = "ForumQueryDTO", value = "标签列表", required = true, dataTypeClass = ForumQueryVO.class, paramType = "body")
    @PostMapping(value = "/list")
    public RestResponse<List<LabelVO>> list() {

        List<LabelVO> labelVOList = labelService.findAll();

        return RestResponse.success(labelVOList);
    }
}
