package com.tree.clouds.assessment.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.common.RestResponse;
import com.tree.clouds.assessment.common.aop.Log;
import com.tree.clouds.assessment.model.entity.ComprehensiveAssessment;
import com.tree.clouds.assessment.model.vo.*;
import com.tree.clouds.assessment.service.ComprehensiveAssessmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 综合评定表 前端控制器
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@RestController
@RequestMapping("/comprehensive-assessment")
@Api(tags ="考评得分管理模块")
public class ComprehensiveAssessmentController {

    @Autowired
    private ComprehensiveAssessmentService assessmentService;

    @Log("成绩管理分页")
    @PostMapping("/performancePage")
    @ApiOperation(value = "成绩管理分页")
    @PreAuthorize("hasAuthority('user:manage:list')")
    public RestResponse<IPage<PerformanceVO>> performancePage(@RequestBody PerformancePageVO performancePageVO) {
        IPage<PerformanceVO> page=assessmentService.performancePage(performancePageVO);
        return RestResponse.ok(page);
    }

    @Log("综合评定")
    @PostMapping("/assessment")
    @ApiOperation(value = "综合评定")
    @PreAuthorize("hasAuthority('user:manage:list')")
    public RestResponse<Boolean> assessment(@RequestBody ComprehensiveAssessmentVO assessmentVO) {
        assessmentService.assessment(assessmentVO);
        return RestResponse.ok(true);
    }

    @Log("结果复评")
    @PostMapping("/reEvaluation")
    @ApiOperation(value = "结果复评")
    @PreAuthorize("hasAuthority('user:manage:list')")
    public RestResponse<Boolean> reEvaluation(@RequestBody ReEvaluationVO evaluationVO) {
        assessmentService.reEvaluation(evaluationVO);
        return RestResponse.ok(true);
    }

}

