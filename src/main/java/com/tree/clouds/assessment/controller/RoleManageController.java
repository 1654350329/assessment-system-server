package com.tree.clouds.assessment.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.common.RestResponse;
import com.tree.clouds.assessment.common.aop.Log;
import com.tree.clouds.assessment.model.entity.RoleManage;
import com.tree.clouds.assessment.model.vo.DistributeRoleVO;
import com.tree.clouds.assessment.model.vo.PublicIdsReqVO;
import com.tree.clouds.assessment.service.RoleManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * <p>
 * 角色管理表 前端控制器
 * </p>
 *
 * @author lzk
 * @since 2021-12-28
 */
@RestController
@RequestMapping("/role-manage")
@Api(value = "role-manage", tags = "角色管理模块")
public class RoleManageController {
    @Autowired
    private RoleManageService roleManageService;

    @PostMapping("/addRole")
    @ApiOperation(value = "添加角色")
    @Log("添加角色")
    @PreAuthorize("hasAuthority('role:manage:add')")
    public RestResponse<Boolean> addRole(@RequestBody RoleManage roleManage) {
        roleManage.setRoleCode(UUID.randomUUID().toString());
        roleManageService.save(roleManage);
        return RestResponse.ok(true);
    }

    @PostMapping("/updateRole")
    @ApiOperation(value = "修改角色")
    @Log("修改角色")
    @PreAuthorize("hasAuthority('role:manage:update')")
    public RestResponse<Boolean> updateRole(@RequestBody RoleManage roleManage) {
        roleManageService.updateById(roleManage);
        return RestResponse.ok(true);
    }



    @PostMapping("/distributeRole")
    @ApiOperation(value = "配置权限")
    @Log("配置权限")
    public RestResponse<Boolean> distributeRole(@RequestBody DistributeRoleVO distributeRoleVO) {
        roleManageService.distributeRole(distributeRoleVO);
        return RestResponse.ok(true);
    }




}

