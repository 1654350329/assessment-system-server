package com.tree.clouds.assessment.controller;


import com.tree.clouds.assessment.common.RestResponse;
import com.tree.clouds.assessment.common.aop.Log;
import com.tree.clouds.assessment.model.vo.PublicIdsReqVO;
import com.tree.clouds.assessment.model.vo.UnitVO;
import com.tree.clouds.assessment.model.vo.IndicatorsTreeTreeVO;
import com.tree.clouds.assessment.service.AssessmentIndicatorsService;
import com.tree.clouds.assessment.service.UnitAssessmentService;
import com.tree.clouds.assessment.utils.LoginUserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(tags = "责任单位设置模块")
public class UnitAssessmentController {
    @Autowired
    private UnitAssessmentService unitAssessmentService;
    @Autowired
    private AssessmentIndicatorsService assessmentIndicatorsService;

    @Log("责任单位列表")
    @PostMapping("/assessmentList")
    @ApiOperation(value = "责任单位列表")
    public RestResponse<List<UnitVO>> assessmentList(@RequestBody UnitVO unitVO) {
        List<UnitVO> units = unitAssessmentService.assessmentList(unitVO);
        return RestResponse.ok(units);
    }

    @Log("各目录树")
    @PostMapping("/indicatorsTree/{year}/{unitId}")
    @ApiOperation(value = "目录树")
    public RestResponse<List<IndicatorsTreeTreeVO>> indicatorsTree(@PathVariable Integer year, @PathVariable String unitId) {
        List<IndicatorsTreeTreeVO> tree = assessmentIndicatorsService.indicatorsTree(year,2,unitId, null);
        return RestResponse.ok(tree);
    }

    @Log("添加指标任务")
    @PostMapping("/addAssessment/{unitId}")
    @ApiOperation(value = "添加指标任务")
    public RestResponse<Boolean> addAssessment(@RequestBody PublicIdsReqVO publicIdsReqVO, @PathVariable String unitId) {
        unitAssessmentService.addAssessment(publicIdsReqVO.getIds(), unitId);
        return RestResponse.ok(true);
    }

    @Log("获取已分配指标任务")
    @PostMapping("/getAssessment/{unitId}")
    @ApiOperation(value = "获取已分配指标任务")
    public RestResponse<List<String>> getAssessment(@PathVariable String unitId) {
        List<String> list = unitAssessmentService.getAssessment(unitId);
        return RestResponse.ok(list);
    }




}

