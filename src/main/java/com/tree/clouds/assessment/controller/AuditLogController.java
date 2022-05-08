package com.tree.clouds.assessment.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.common.RestResponse;
import com.tree.clouds.assessment.common.aop.Log;
import com.tree.clouds.assessment.model.vo.*;
import com.tree.clouds.assessment.service.AuditLogService;
import com.tree.clouds.assessment.service.IndicatorReportService;
import com.tree.clouds.assessment.service.SubmitLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @Log("单位考核指标列表")
    @PostMapping("/assessmentList")
    @ApiOperation(value = "单位考核指标列表")
    @PreAuthorize("hasAuthority('user:manage:list')")
    public RestResponse<IPage<IndicatorReportVO>> assessmentList(@RequestBody AssessmentPageVO assessmentPageVO) {
        IPage<IndicatorReportVO> assessmentVOS = indicatorReportService.assessmentList(assessmentPageVO,1);
        return RestResponse.ok(assessmentVOS);
    }

    @Log("初审审核")
    @PostMapping("/updateAudit")
    @ApiOperation(value = "初审审核")
    @PreAuthorize("hasAuthority('user:manage:list')")
    public RestResponse<Boolean> updateAudit(@RequestBody UpdateAuditVO updateAuditVO) {
        auditLogService.updateAudit(updateAuditVO);
        return RestResponse.ok(true);
    }

    @Log("报送日志")
    @PostMapping("/submitLogPage")
    @ApiOperation(value = "报送日志")
    @PreAuthorize("hasAuthority('user:manage:list')")
    public RestResponse<IPage<SubmitLogVO>> submitLogPage(@RequestBody SubmitLogPageVO submitLogPageVO) {
        IPage<SubmitLogVO> page=submitLogService.submitLogPage(submitLogPageVO);
        return RestResponse.ok(page);
    }
}

