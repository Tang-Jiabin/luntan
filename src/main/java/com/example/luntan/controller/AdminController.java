package com.example.luntan.controller;

import com.example.luntan.common.RestResponse;
import com.example.luntan.dto.AdminDTO;
import com.example.luntan.dto.UserDTO;
import com.example.luntan.pojo.Forum;
import com.example.luntan.pojo.Label;
import com.example.luntan.pojo.User;
import com.example.luntan.service.AdminService;
import com.example.luntan.service.ForumService;
import com.example.luntan.service.LabelService;
import com.example.luntan.service.UserService;
import com.example.luntan.vo.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("admin")
@AllArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final ForumService forumService;
    private final UserService userService;
    private final LabelService labelService;


    @ApiOperation("登录")
    @ApiImplicitParam(name = "AdminDTO", value = "登录", required = true, dataTypeClass = AdminDTO.class, paramType = "body")
    @PostMapping(value = "/login")
    public RestResponse<AdminDTO> login(@RequestBody AdminDTO adminDTO) {
        AdminVO adminVO = adminService.login(adminDTO);
        return RestResponse.success(adminVO);
    }

    @ApiOperation("统计")
    @ApiImplicitParam(name = "AdminDTO", value = "统计", required = true, dataTypeClass = AdminDTO.class, paramType = "body")
    @PostMapping(value = "/statistics")
    public RestResponse<Map> statistics(@RequestBody AdminDTO adminDTO) {
        AdminVO adminVO = adminService.login(adminDTO);
        Map statistics = adminService.statistics(adminDTO);
        return RestResponse.success(statistics);
    }


    @ApiOperation("帖子列表")
    @ApiImplicitParam(name = "AdminDTO", value = "帖子列表", required = true, dataTypeClass = AdminDTO.class, paramType = "body")
    @PostMapping(value = "/tie/List")
    public RestResponse<PageVO<ForumVO>> tieList(@RequestBody AdminForumVO adminForumVO) {
        AdminDTO adminDTO = new AdminDTO();
        BeanUtils.copyProperties(adminForumVO,adminDTO);
        AdminVO adminVO = adminService.login(adminDTO);
        ForumQueryVO forumQueryVO = new ForumQueryVO();
        BeanUtils.copyProperties(adminForumVO,forumQueryVO);
        Page<Forum> page = forumService.findPage(forumQueryVO);
        PageVO<ForumVO> pageVO = forumService.page2VO(page);
        pageVO.setPage(adminForumVO.getPage());
        List<UserDTO> userDTOList = userService.findAllByIdList(page.get().map(Forum::getUid).collect(Collectors.toList()));
        List<ForumVO> forumVOList = forumService.forumList2VO(page.getContent(), userDTOList, forumQueryVO.getLoginId());
        pageVO.setContent(forumVOList);
        return RestResponse.success(pageVO);
    }

    @ApiOperation("标签列表")
    @ApiImplicitParam(name = "AdminDTO", value = "标签列表", required = true, dataTypeClass = AdminDTO.class, paramType = "body")
    @PostMapping(value = "/label/List")
    public RestResponse<List<LabelVO>> labelList(@RequestBody AdminDTO adminDTO) {
        AdminVO adminVO = adminService.login(adminDTO);
        List<LabelVO> labelVOList = labelService.findAll();
        return RestResponse.success(labelVOList);
    }

    @ApiOperation("用户列表")
    @ApiImplicitParam(name = "AdminDTO", value = "用户列表", required = true, dataTypeClass = AdminDTO.class, paramType = "body")
    @PostMapping(value = "/user/List")
    public RestResponse<PageVO<UserVO>> userList(@RequestBody AdminForumVO adminForumVO) {
        AdminDTO adminDTO = new AdminDTO();
        BeanUtils.copyProperties(adminForumVO, adminDTO);
        AdminVO adminVO = adminService.login(adminDTO);
        Page<User> userPage = userService.findPage(adminForumVO.getPage(), adminForumVO.getLimit(), adminForumVO.getTitle());
        PageVO<UserVO> pageVO = userService.page2VO(userPage);
        pageVO.setPage(adminForumVO.getPage());
        List<UserVO> userVOList = userService.list2VO(userPage.getContent());
        pageVO.setContent(userVOList);
        return RestResponse.success(pageVO);
    }

    @ApiOperation("修改密码")
    @ApiImplicitParam(name = "AdminDTO", value = "修改密码", required = true, dataTypeClass = AdminDTO.class, paramType = "body")
    @PostMapping(value = "/user/update")
    public RestResponse<PageVO<UserVO>> userUpdate(@RequestBody AdminDTO adminDTO) {
        adminService.update(adminDTO.getName(), adminDTO.getNickname(), adminDTO.getPwd());
        return RestResponse.success();
    }

    @ApiOperation("标签列表")
    @ApiImplicitParam(name = "AdminDTO", value = "标签列表", required = true, dataTypeClass = AdminDTO.class, paramType = "body")
    @PostMapping(value = "/label/page")
    public RestResponse<PageVO<LabelVO>> labelPage(@RequestBody AdminForumVO adminForumVO) {
        AdminDTO adminDTO = new AdminDTO();
        BeanUtils.copyProperties(adminForumVO, adminDTO);
        AdminVO adminVO = adminService.login(adminDTO);
        Page<Label> labelPage = labelService.findPage(adminForumVO.getPage(), adminForumVO.getLimit(), adminForumVO.getTitle());
        PageVO<LabelVO> pageVO = labelService.page2VO(labelPage);
        pageVO.setPage(adminForumVO.getPage());
        List<LabelVO> labelVOList = labelService.list2vo(labelPage.getContent());
        pageVO.setContent(labelVOList);
        return RestResponse.success(pageVO);
    }

    @ApiOperation("删除标签")
    @ApiImplicitParam(name = "AdminDTO", value = "删除标签", required = true, dataTypeClass = AdminDTO.class, paramType = "body")
    @PostMapping(value = "/label/del")
    public RestResponse<Object> labelDel(@RequestBody AdminDTO adminDTO) {
        labelService.del(adminDTO.getId());
        return RestResponse.success();
    }

    @ApiOperation("删除用户")
    @ApiImplicitParam(name = "AdminDTO", value = "删除用户", required = true, dataTypeClass = AdminDTO.class, paramType = "body")
    @PostMapping(value = "/user/del")
    public RestResponse<Object> userDel(@RequestBody AdminDTO adminDTO) {
        userService.del(adminDTO.getId());
        return RestResponse.success();
    }

    @ApiOperation("删除帖子")
    @ApiImplicitParam(name = "AdminDTO", value = "删除帖子", required = true, dataTypeClass = AdminDTO.class, paramType = "body")
    @PostMapping(value = "/tie/del")
    public RestResponse<Object> tieDel(@RequestBody AdminDTO adminDTO) {
        forumService.del(adminDTO.getId());
        return RestResponse.success();
    }
}
