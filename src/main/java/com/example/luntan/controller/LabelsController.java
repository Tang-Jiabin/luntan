package com.example.luntan.controller;

import com.example.luntan.common.RestResponse;
import com.example.luntan.vo.ForumQueryVO;
import com.example.luntan.vo.ForumVO;
import com.example.luntan.vo.LabelVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("Labels")
public class LabelsController {

    @ApiOperation("标签列表")
    @ApiImplicitParam(name = "ForumQueryDTO", value = "标签列表", required = true, dataTypeClass = ForumQueryVO.class, paramType = "body")
    @PostMapping(value = "/list")
    public RestResponse list(@RequestBody ForumQueryVO forumQueryVO) {



        LabelVO labelVO = new LabelVO();
        labelVO.setId(1);
        labelVO.setName("标签1");
        labelVO.setIoc("");
        List list2 = new ArrayList();
        list2.add(labelVO);
        labelVO = new LabelVO();
        labelVO.setId(2);
        labelVO.setName("标签2");
        labelVO.setIoc("");


        list2.add(labelVO);


        return RestResponse.success(list2);
    }
}
