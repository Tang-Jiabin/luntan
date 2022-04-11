package com.example.luntan.controller;

import com.example.luntan.common.RestResponse;
import com.example.luntan.dto.ForumDTO;
import com.example.luntan.dto.UserDTO;
import com.example.luntan.pojo.Forum;
import com.example.luntan.pojo.Jl;
import com.example.luntan.pojo.Pl;
import com.example.luntan.pojo.Sc;
import com.example.luntan.service.ForumService;
import com.example.luntan.service.PlService;
import com.example.luntan.service.UserService;
import com.example.luntan.vo.*;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("Forum")
public class ForumController {


    private final ForumService forumService;
    private final UserService userService;
    private final PlService plService;

    public ForumController(ForumService forumService, UserService userService, PlService plService) {
        this.forumService = forumService;
        this.userService = userService;
        this.plService = plService;
    }

    @ApiOperation("帖子列表")
    @ApiImplicitParam(name = "ForumQueryDTO", value = "帖子列表", required = true, dataTypeClass = ForumQueryVO.class, paramType = "body")
    @PostMapping(value = "/list")
    public RestResponse<PageVO<ForumVO>> list(@RequestBody ForumQueryVO forumQueryVO) {
        PageVO<ForumVO> pageVO = getForumPageVO(forumQueryVO);
        return RestResponse.success(pageVO);
    }


    @ApiOperation("收藏列表")
    @ApiImplicitParam(name = "ForumQueryDTO", value = "收藏列表", required = true, dataTypeClass = ForumQueryVO.class, paramType = "body")
    @PostMapping(value = "/sclist")
    public RestResponse<PageVO<ForumVO>> sclist(@RequestBody ForumQueryVO forumQueryVO) {
        List<Sc> scList = forumService.findScList(forumQueryVO.getLoginId());
        scList.add(new Sc(0, 0, 0));
        forumQueryVO.setIds(scList.stream().map(Sc::getFid).collect(Collectors.toList()));
        PageVO<ForumVO> pageVO = getForumPageVO(forumQueryVO);
        return RestResponse.success(pageVO);
    }

    @ApiOperation("历史列表")
    @ApiImplicitParam(name = "ForumQueryDTO", value = "历史列表", required = true, dataTypeClass = ForumQueryVO.class, paramType = "body")
    @PostMapping(value = "/jllist")
    public RestResponse<PageVO<ForumVO>> jllist(@RequestBody ForumQueryVO forumQueryVO) {
        List<Jl> scList = forumService.findJlList(forumQueryVO.getLoginId());
        scList.add(new Jl(0, 0, 0, Instant.now()));
        forumQueryVO.setIds(scList.stream().map(Jl::getFid).collect(Collectors.toList()));
        PageVO<ForumVO> pageVO = getForumPageVO(forumQueryVO);
        return RestResponse.success(pageVO);
    }

    @NotNull
    private PageVO<ForumVO> getForumPageVO(ForumQueryVO forumQueryVO) {
        Page<Forum> page = forumService.findPage(forumQueryVO);
        PageVO<ForumVO> pageVO = forumService.page2VO(page);
        List<UserDTO> userDTOList = userService.findAllByIdList(page.get().map(Forum::getUid).collect(Collectors.toList()));
        List<ForumVO> forumVOList = forumService.forumList2VO(page.getContent(), userDTOList, forumQueryVO.getLoginId());
        pageVO.setContent(forumVOList);
        return pageVO;
    }


    @ApiOperation("点赞")
    @ApiImplicitParam(name = "ItemIdDTO", value = "点赞", required = true, dataTypeClass = ItemIdVO.class, paramType = "body")
    @PostMapping(value = "/dz")
    public RestResponse<Object> dz(@RequestBody ItemIdVO itemIdVO) {
        forumService.dz(itemIdVO.getUid(), itemIdVO.getId());
        return RestResponse.success();
    }

    @ApiOperation("取消点赞")
    @ApiImplicitParam(name = "ItemIdDTO", value = "取消点赞", required = true, dataTypeClass = ItemIdVO.class, paramType = "body")
    @PostMapping(value = "/dzdel")
    public RestResponse<Object> dzdel(@RequestBody ItemIdVO itemIdVO) {
        forumService.dz(itemIdVO.getUid(), itemIdVO.getId());
        return RestResponse.success();
    }

    @ApiOperation("收藏")
    @ApiImplicitParam(name = "ItemIdDTO", value = "收藏", required = true, dataTypeClass = ItemIdVO.class, paramType = "body")
    @PostMapping(value = "/sc")
    public RestResponse<Object> sc(@RequestBody ItemIdVO itemIdVO) {
        forumService.sc(itemIdVO.getUid(), itemIdVO.getId());
        return RestResponse.success();
    }

    @ApiOperation("取消收藏")
    @ApiImplicitParam(name = "ItemIdDTO", value = "取消收藏", required = true, dataTypeClass = ItemIdVO.class, paramType = "body")
    @PostMapping(value = "/scdel")
    public RestResponse<Object> scdel(@RequestBody ItemIdVO itemIdVO) {
        forumService.sc(itemIdVO.getUid(), itemIdVO.getId());
        return RestResponse.success();
    }

    @ApiOperation("详情")
    @ApiImplicitParam(name = "ItemIdDTO", value = "详情", required = true, dataTypeClass = ItemIdVO.class, paramType = "body")
    @PostMapping(value = "/bydata")
    public RestResponse<ForumVO> bydata(@RequestBody ItemIdVO itemIdVO) {
        ForumDTO forumDTO = forumService.findById(itemIdVO.getId());
        UserDTO userDTO = userService.findById(forumDTO.getUid());
        ForumVO forumVO = forumService.dto2vo(forumDTO, userDTO, itemIdVO.getUid());
        forumService.addJl(itemIdVO);
        return RestResponse.success(forumVO);
    }

    @ApiOperation("评论列表")
    @ApiImplicitParam(name = "ItemIdDTO", value = "评论列表", required = true, dataTypeClass = ItemIdVO.class, paramType = "body")
    @PostMapping(value = "/pllist")
    public RestResponse pllist(@RequestBody PlQueryVO plQueryVO) {

        Page<Pl> plPage = plService.findPage(plQueryVO);
        PageVO<PlVO> plPageVO = plService.page2vo(plPage);
        List<UserDTO> userDTOList = userService.findAllByIdList(plPage.get().map(Pl::getUid).collect(Collectors.toList()));
        List<PlVO> plVOList = plService.plAddUserInfo(plPageVO.getContent(), userDTOList);
        plPageVO.setContent(plVOList);
        return RestResponse.success(plPageVO);
    }

    @ApiOperation("添加评论")
    @ApiImplicitParam(name = "PlAddDTO", value = "添加评论", required = true, dataTypeClass = PlAddVO.class, paramType = "body")
    @PostMapping(value = "/plAdd")
    public RestResponse<Object> plAdd(@RequestBody PlAddVO plAddVO) {
        if (!StringUtils.hasText(plAddVO.getContent())) {
            return RestResponse.error(411, "请输入评论内容");
        }
        plService.add(plAddVO);
        return RestResponse.success();
    }


    @ApiOperation("用户数据")
    @ApiImplicitParam(name = "ItemIdDTO", value = "用户数据", required = true, dataTypeClass = ItemIdVO.class, paramType = "body")
    @PostMapping(value = "/userdata")
    public RestResponse<UserForumVO> userdata(@RequestBody ItemIdVO itemIdVO) {
        UserForumVO userForumVO = forumService.findUserDataStatistics(itemIdVO.getUid());

        return RestResponse.success(userForumVO);
    }


    @ApiOperation("发布帖子")
    @ApiImplicitParam(name = "ItemIdDTO", value = "发布帖子", required = true, dataTypeClass = ItemIdVO.class, paramType = "body")
    @PostMapping(value = "/add")
    public RestResponse<UserForumVO> add(@RequestBody ForumVO forumVO) {
        ForumDTO forumDTO = new ForumDTO();
        BeanUtils.copyProperties(forumVO, forumDTO);
        forumService.add(forumDTO);
        return RestResponse.success();
    }

}
