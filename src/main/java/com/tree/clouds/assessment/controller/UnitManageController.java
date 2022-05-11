package com.tree.clouds.assessment.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.common.RestResponse;
import com.tree.clouds.assessment.common.aop.Log;
import com.tree.clouds.assessment.model.entity.UnitManage;
import com.tree.clouds.assessment.model.vo.UnitManagePageVO;
import com.tree.clouds.assessment.model.vo.PublicIdsReqVO;
import com.tree.clouds.assessment.service.UnitManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 单位管理 前端控制器
 * </p>
 *
 * @author lzk
 * @since 2021-12-28
 */
@RestController
@RequestMapping("/group-manage")
@Api(value = "group-manage", tags = "单位管理模块")
public class UnitManageController {

    @Autowired
    private UnitManageService unitManageService;

    @PostMapping("/groupManagePage")
    @ApiOperation(value = "单位管理模块分页查询")
    @Log("单位管理模块分页查询")

    public RestResponse<IPage<UnitManage>> groupManagePage(@RequestBody UnitManagePageVO unitManagePageVO) {
        IPage<UnitManage> page = unitManageService.groupManagePage(unitManagePageVO);
        return RestResponse.ok(page);
    }

    @PostMapping("/addGroupRole")
    @ApiOperation(value = "添加单位")
    @Log("添加单位")
    public RestResponse<Boolean> addGroupRole(@RequestBody UnitManage groupRole) {
        unitManageService.save(groupRole);
        return RestResponse.ok(true);
    }

    @PostMapping("/updateGroupRole")
    @ApiOperation(value = "修改单位")
    @Log("修改单位")

    public RestResponse<Boolean> updateGroupRole(@RequestBody UnitManage groupRole) {
        unitManageService.updateById(groupRole);
        return RestResponse.ok(true);
    }

    @PostMapping("/deleteGroupRole")
    @ApiOperation(value = "刪除单位")
    @Log("刪除单位")

    public RestResponse<Boolean> deleteGroupRole(@Validated @RequestBody PublicIdsReqVO publicIdReqVO) {
        unitManageService.deleteGroupRole(publicIdReqVO.getIds());
        return RestResponse.ok(true);
    }
}

