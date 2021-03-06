package com.tree.clouds.assessment.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.common.RestResponse;
import com.tree.clouds.assessment.common.aop.Log;
import com.tree.clouds.assessment.model.bo.UserManageBO;
import com.tree.clouds.assessment.model.vo.PublicIdsReqVO;
import com.tree.clouds.assessment.model.vo.UpdatePasswordVO;
import com.tree.clouds.assessment.model.vo.UserManagePageVO;
import com.tree.clouds.assessment.model.vo.UserStatusVO;
import com.tree.clouds.assessment.service.UserManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 用户管理 前端控制器
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@RestController
@RequestMapping("/user-manage")
@Api(tags = "用户管理模块")
public class UserManageController {

    @Autowired
    private UserManageService userManageservice;

    @Log("用户模块分页查询")
    @PostMapping("/userManagePage")
    @ApiOperation(value = "用户模块分页查询")
    public RestResponse<IPage<UserManageBO>> userManagePage(@Validated @RequestBody UserManagePageVO userManagePageVO) {
        IPage<UserManageBO> page = userManageservice.userManagePage(userManagePageVO);
        return RestResponse.ok(page);
    }

    @PostMapping("/addUserManage")
    @ApiOperation(value = "添加用户")
    @Log("添加用户")
    public RestResponse<Boolean> addUserManage(@Validated @RequestBody UserManageBO userManageBO) {
        userManageservice.addUserManage(userManageBO);
        return RestResponse.ok(true);
    }

    @PostMapping("/updateUserManage")
    @ApiOperation(value = "修改用户")
    @Log("修改用户")
    public RestResponse<Boolean> updateUserManage(@Validated @RequestBody UserManageBO userManageBO) {
        userManageservice.updateUserManage(userManageBO);
        return RestResponse.ok(true);
    }

    @PostMapping("/deleteUserManage")
    @ApiOperation(value = "刪除用户")
    @Log("刪除用户")
    public RestResponse<Boolean> deleteUserManage(@Validated @RequestBody PublicIdsReqVO publicIdReqVO) {
        for (String id : publicIdReqVO.getIds()) {
            userManageservice.deleteUserManage(id);
        }
        return RestResponse.ok(true);
    }

    @PostMapping("/rebuildPassword")
    @ApiOperation(value = "重置密码")
    @Log("重置密码")
    public RestResponse<Boolean> rebuildPassword(@RequestBody PublicIdsReqVO publicIdsReqVO) {
        userManageservice.rebuildPassword(publicIdsReqVO.getIds());
        return RestResponse.ok(true);
    }

    @PostMapping("/userStatus")
    @ApiOperation(value = "启用或停用用户")
    @Log("启用或停用用户")
    public RestResponse<Boolean> userStatus(@Validated @RequestBody UserStatusVO userStatusVO) {
        userManageservice.userStatus(userStatusVO.getIds(), userStatusVO.getStatus());
        return RestResponse.ok(true);
    }

    @PostMapping("/updatePassword")
    @ApiOperation(value = "修改当前用户密码")
    @Log("修改当前用户密码")
    public RestResponse<Boolean> updatePassword(@RequestBody UpdatePasswordVO updatePasswordVO) {
        userManageservice.updatePassword(updatePasswordVO);
        return RestResponse.ok(true);
    }
}

