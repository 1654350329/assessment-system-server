package com.tree.clouds.assessment.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.common.RestResponse;
import com.tree.clouds.assessment.common.aop.Log;
import com.tree.clouds.assessment.model.entity.AuditLog;
import com.tree.clouds.assessment.model.vo.*;
import com.tree.clouds.assessment.service.AssessmentIndicatorsService;
import com.tree.clouds.assessment.service.AuditLogService;
import com.tree.clouds.assessment.service.IndicatorReportService;
import com.tree.clouds.assessment.service.SubmitLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 审核日志 前端控制器
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@RestController
@RequestMapping("/audit-log")
@Api(tags = "初审管理模块")
public class AuditLogController {
    @Autowired
    private AuditLogService auditLogService;
    @Autowired
    private IndicatorReportService indicatorReportService;

    @Autowired
    private SubmitLogService submitLogService;
    @Autowired
    private AssessmentIndicatorsService assessmentIndicatorsService;

    @Log("单位考核指标列表")
    @PostMapping("/assessmentList")
    @ApiOperation(value = "单位考核指标列表")
    public RestResponse<IPage<IndicatorReportVO>> assessmentList(@RequestBody AssessmentPageVO assessmentPageVO) {
        assessmentPageVO.setIndicatorsType(0);
        IPage<IndicatorReportVO> assessmentVOS = indicatorReportService.assessmentList(assessmentPageVO,1);
        return RestResponse.ok(assessmentVOS);
    }


    @Log("单位考核指标列表总分值与自评总分")
    @PostMapping("/getData")
    @ApiOperation(value = "单位考核指标列表总分值与自评总分")
    public RestResponse<Map<String,Object>> getData(@RequestBody GetDataVO getDataVO) {
        Map<String,Object> map = indicatorReportService.getData(getDataVO);
        return RestResponse.ok(map);
    }

    @Log("初审树")
    @PostMapping("/auditTree")
    @ApiOperation(value = "初审树")
    public RestResponse<List<IndicatorsTreeTreeVO>> auditTree(@RequestBody AuditTreeVO auditTreeVO) {
        List<IndicatorsTreeTreeVO> tree = assessmentIndicatorsService.auditTree(auditTreeVO);
        return RestResponse.ok(tree);
    }


    @Log("初审审核")
    @PostMapping("/updateAudit")
    @ApiOperation(value = "初审审核")
    public RestResponse<Boolean> updateAudit(@Validated @RequestBody UpdateAuditVO updateAuditVO) {
        auditLogService.updateAudit(updateAuditVO);
        return RestResponse.ok(true);
    }
    @Log("评分左侧树")
    @PostMapping("/scoreLeftTree/{year}/{unitId}")
    @ApiOperation(value = "评分左侧树")
    public RestResponse<List<IndicatorsTreeTreeVO>> scoreLeftTree(@PathVariable Integer year, @PathVariable String unitId) {
        List<IndicatorsTreeTreeVO> tree = indicatorReportService.scoreLeftTree(year, unitId,1);
        return RestResponse.ok(tree);
    }

    @Log("获取初审信息")
    @PostMapping("/getAudit/{id}")
    @ApiOperation(value = "获取初审信息")
    public RestResponse<AuditLog> getAudit(@PathVariable String id) {
        AuditLog auditLog=auditLogService.getAudit(id);
        return RestResponse.ok(auditLog);
    }

    @Log("报送日志")
    @PostMapping("/submitLogPage")
    @ApiOperation(value = "报送日志")
    public RestResponse<IPage<SubmitLogVO>> submitLogPage(@RequestBody SubmitLogPageVO submitLogPageVO) {
        IPage<SubmitLogVO> page=submitLogService.submitLogPage(submitLogPageVO);
        return RestResponse.ok(page);
    }
}

