package com.tree.clouds.assessment.controller;


import com.tree.clouds.assessment.common.RestResponse;
import com.tree.clouds.assessment.common.aop.Log;
import com.tree.clouds.assessment.model.vo.PublicIdsReqVO;
import com.tree.clouds.assessment.model.vo.UnitVO;
import com.tree.clouds.assessment.service.UnitAssessmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 指标单位中间表 前端控制器
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@RestController
@RequestMapping("/unit-assessment")
@Api(tags = "责任单位模块")
public class UnitAssessmentController {
    @Autowired
    private UnitAssessmentService unitAssessmentService;

    @Log("责任单位列表")
    @PostMapping("/assessmentList")
    @ApiOperation(value = "责任单位列表")
    @PreAuthorize("hasAuthority('user:manage:list')")
    public RestResponse<List<UnitVO>> assessmentList(@RequestBody UnitVO unitVO) {
        List<UnitVO> units = unitAssessmentService.assessmentList(unitVO);
        return RestResponse.ok(units);
    }

    @Log("添加指标任务")
    @PostMapping("/addAssessment/{unitId}")
    @ApiOperation(value = "添加指标任务")
    @PreAuthorize("hasAuthority('user:manage:list')")
    public RestResponse<Boolean> addAssessment(@RequestBody PublicIdsReqVO publicIdsReqVO, @PathVariable String unitId) {
        unitAssessmentService.addAssessment(publicIdsReqVO.getIds(), unitId);
        return RestResponse.ok(true);
    }

    @Log("获取已分配指标任务")
    @PostMapping("/getAssessment/{unitId}")
    @ApiOperation(value = "获取已分配指标任务")
    @PreAuthorize("hasAuthority('user:manage:list')")
    public RestResponse<List<String>> getAssessment(@PathVariable String unitId) {
        List<String> list = unitAssessmentService.getAssessment(unitId);
        return RestResponse.ok(list);
    }

}
