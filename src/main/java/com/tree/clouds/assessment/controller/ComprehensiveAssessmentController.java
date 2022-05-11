package com.tree.clouds.assessment.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.common.RestResponse;
import com.tree.clouds.assessment.common.aop.Log;
import com.tree.clouds.assessment.model.entity.AssessmentCondition;
import com.tree.clouds.assessment.model.entity.ComprehensiveAssessment;
import com.tree.clouds.assessment.model.vo.*;
import com.tree.clouds.assessment.service.AssessmentConditionService;
import com.tree.clouds.assessment.service.ComprehensiveAssessmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
@Api(tags = "考评得分管理模块")
public class ComprehensiveAssessmentController {

    @Autowired
    private ComprehensiveAssessmentService assessmentService;
    @Autowired
    private AssessmentConditionService assessmentConditionService;

    @Log("成绩管理分页")
    @PostMapping("/performancePage")
    @ApiOperation(value = "成绩管理分页")
    public RestResponse<IPage<ComprehensiveAssessment>> performancePage(@RequestBody PerformancePageVO performancePageVO) {
        IPage<ComprehensiveAssessment> page = assessmentService.performancePage(performancePageVO);
        return RestResponse.ok(page);
    }

    @Log("综合评定")
    @PostMapping("/assessment")
    @ApiOperation(value = "综合评定")
    public RestResponse<Boolean> assessment(@RequestBody ComprehensiveAssessmentVO assessmentVO) {
        assessmentService.assessment(assessmentVO);
        return RestResponse.ok(true);
    }
    @Log("复核")
    @PostMapping("/reAssessment")
    @ApiOperation(value = "复核")
    public RestResponse<Boolean> reAssessment(@RequestBody ReAssessmentVO reAssessmentVO) {
        assessmentService.reAssessment(reAssessmentVO);
        return RestResponse.ok(true);
    }


    @Log("完成考核")
    @PostMapping("/assessmentComplete")
    @ApiOperation(value = "完成考核")
    public RestResponse<Boolean> assessmentComplete(@RequestBody AssessmentCompleteVO assessmentCompleteVO) {
        assessmentService.assessmentComplete(assessmentCompleteVO);
        return RestResponse.ok(true);
    }

    @Log("加减分一览表")
    @PostMapping("/conditionPage")
    @ApiOperation(value = "加减分一览表")
    public RestResponse<IPage<ConditionVO>> conditionPage(@RequestBody ScorePageVO scorePageVO) {
        IPage<ConditionVO> page = assessmentConditionService.conditionPage(scorePageVO);
        return RestResponse.ok(page);
    }

    @Log("导出一览表")
    @PostMapping("/exportDate")
    @ApiOperation(value = "导出一览表")
    public void exportDate(@RequestBody GetConditionVO getConditionVO, HttpServletResponse response) {
        assessmentConditionService.exportDate(getConditionVO,response);
    }

    @Log("加减分一览表查看明细")
    @PostMapping("/getConditionList")
    @ApiOperation(value = "加减分一览表查看明细")
    public RestResponse<List<AssessmentCondition>> getConditionList(@RequestBody GetConditionVO getConditionVO) {
        return RestResponse.ok(assessmentConditionService.getConditionList(getConditionVO));
    }

}

