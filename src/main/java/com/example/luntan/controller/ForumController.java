package com.example.luntan.controller;

import com.example.luntan.common.RestResponse;
import com.example.luntan.dto.ForumDTO;
import com.example.luntan.service.ForumService;
import com.example.luntan.vo.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("Forum")
public class ForumController {


    private final ForumService forumService;

    public ForumController(ForumService forumService) {
        this.forumService = forumService;
    }

    @ApiOperation("帖子列表")
    @ApiImplicitParam(name = "ForumQueryDTO", value = "查询信息", required = true, dataTypeClass = ForumQueryVO.class, paramType = "body")
    @PostMapping(value = "/list")
    public RestResponse<PageVO<ForumVO>> list(@RequestBody ForumQueryVO forumQueryVO) {
        PageVO<ForumVO> page = forumService.findPage(forumQueryVO);
        return RestResponse.success(page);
    }

    @ApiOperation("点赞")
    @ApiImplicitParam(name = "ItemIdDTO", value = "点赞", required = true, dataTypeClass = ItemIdVO.class, paramType = "body")
    @PostMapping(value = "/dz")
    public RestResponse dz(@RequestBody ItemIdVO itemIdVO) {
        forumService.dz(itemIdVO.getUid(),itemIdVO.getId());
        return RestResponse.success();
    }

    @ApiOperation("取消点赞")
    @ApiImplicitParam(name = "ItemIdDTO", value = "取消点赞", required = true, dataTypeClass = ItemIdVO.class, paramType = "body")
    @PostMapping(value = "/dzdel")
    public RestResponse dzdel(@RequestBody ItemIdVO itemIdVO) {
        forumService.dz(itemIdVO.getUid(),itemIdVO.getId());
        return RestResponse.success();
    }

    @ApiOperation("收藏")
    @ApiImplicitParam(name = "ItemIdDTO", value = "收藏", required = true, dataTypeClass = ItemIdVO.class, paramType = "body")
    @PostMapping(value = "/sc")
    public RestResponse sc(@RequestBody ItemIdVO itemIdVO) {
        forumService.sc(itemIdVO.getUid(),itemIdVO.getId());
        return RestResponse.success();
    }

    @ApiOperation("取消收藏")
    @ApiImplicitParam(name = "ItemIdDTO", value = "取消收藏", required = true, dataTypeClass = ItemIdVO.class, paramType = "body")
    @PostMapping(value = "/scdel")
    public RestResponse scdel(@RequestBody ItemIdVO itemIdVO) {
        forumService.sc(itemIdVO.getUid(),itemIdVO.getId());
        return RestResponse.success();
    }

    @ApiOperation("详情")
    @ApiImplicitParam(name = "ItemIdDTO", value = "详情", required = true, dataTypeClass = ItemIdVO.class, paramType = "body")
    @PostMapping(value = "/bydata")
    public RestResponse bydata(@RequestBody ItemIdVO itemIdVO) {

        ForumVO forumVO = new ForumVO();
        forumVO.setId(1);
        forumVO.setLogo("https://img-blog.csdn.net/20180611163559620");
        forumVO.setUname("用户名");
        forumVO.setCtime("2022-1-1");
        forumVO.setContent("帖子内容");
        forumVO.setPicture("https://img-blog.csdn.net/20180611163559620,https://img-blog.csdn.net/20180611163559620");
        forumVO.setDz(1);
        forumVO.setDzmun(12);
        forumVO.setPlmun(2);
        forumVO.setSc(1);
        return RestResponse.success(forumVO);
    }

    @ApiOperation("评论列表")
    @ApiImplicitParam(name = "ItemIdDTO", value = "评论列表", required = true, dataTypeClass = ItemIdVO.class, paramType = "body")
    @PostMapping(value = "/pllist")
    public RestResponse pllist(@RequestBody ItemIdVO itemIdVO) {
        List<ForumVO> list = new ArrayList();
        ForumVO forumVO = new ForumVO();
        forumVO.setId(1);
        forumVO.setLogo("https://img-blog.csdn.net/20180611163559620");
        forumVO.setUname("用户名2");
        forumVO.setCtime("2022-1-1");
        forumVO.setContent("评论评论评论评论评论评论评论");
        forumVO.setPicture("https://img-blog.csdn.net/20180611163559620,https://img-blog.csdn.net/20180611163559620");
        forumVO.setDz(1);
        forumVO.setDzmun(12);
        forumVO.setPlmun(2);
        forumVO.setSc(1);
        list.add(forumVO);

        return RestResponse.success(list);
    }

    @ApiOperation("添加评论")
    @ApiImplicitParam(name = "PlAddDTO", value = "添加评论", required = true, dataTypeClass = PlAddVO.class, paramType = "body")
    @PostMapping(value = "/plAdd")
    public RestResponse plAdd(@RequestBody PlAddVO plAddDTO) {


        return RestResponse.success();
    }


    @ApiOperation("用户数据")
    @ApiImplicitParam(name = "ItemIdDTO", value = "添加评论", required = true, dataTypeClass = ItemIdVO.class, paramType = "body")
    @PostMapping(value = "/userdata")
    public RestResponse<UserForumVO> userdata(@RequestBody ItemIdVO itemIdVO) {
        UserForumVO userForumVO = new UserForumVO();
        userForumVO.setTiezi(1);
        userForumVO.setPl(2);
        userForumVO.setDz(3);
        return RestResponse.success(userForumVO);
    }


    @ApiOperation("发布帖子")
    @ApiImplicitParam(name = "ItemIdDTO", value = "添加评论", required = true, dataTypeClass = ItemIdVO.class, paramType = "body")
    @PostMapping(value = "/add")
    public RestResponse<UserForumVO> add(@RequestBody ForumVO forumVO) {
        ForumDTO forumDTO = new ForumDTO();
        BeanUtils.copyProperties(forumVO,forumDTO);
        forumService.add(forumDTO);
        return RestResponse.success();
    }

}
