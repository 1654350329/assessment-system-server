package com.tree.clouds.assessment.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.common.RestResponse;
import com.tree.clouds.assessment.common.aop.Log;
import com.tree.clouds.assessment.model.entity.IndicatorReport;
import com.tree.clouds.assessment.model.vo.*;
import com.tree.clouds.assessment.service.IndicatorReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 数据上报 前端控制器
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@RestController
@RequestMapping("/indicator-report")
@Api(tags = "考核指标数据上报")
public class IndicatorReportController {
    @Autowired
    private IndicatorReportService indicatorReportService;

    @Log("单位考核指标列表")
    @PostMapping("/assessmentList")
    @ApiOperation(value = "单位考核指标列表")
    @PreAuthorize("hasAuthority('user:manage:list')")
    public RestResponse<IPage<IndicatorReportVO>> assessmentList(@RequestBody AssessmentPageVO assessmentPageVO) {
        IPage<IndicatorReportVO> assessmentVOS = indicatorReportService.assessmentList(assessmentPageVO,0);
        return RestResponse.ok(assessmentVOS);
    }

    @Log("填报数据")
    @PostMapping("/updateReport")
    @ApiOperation(value = "填报数据")
    @PreAuthorize("hasAuthority('user:manage:list')")
    public RestResponse<Boolean> updateReport(@RequestBody UpdateReportVO updateReportVO) {
        indicatorReportService.updateReport(updateReportVO,0);
        return RestResponse.ok(true);
    }

    @Log("驳回列表")
    @PostMapping("/assessmentErrorList")
    @ApiOperation(value = "驳回列表")
    @PreAuthorize("hasAuthority('user:manage:list')")
    public RestResponse<IPage<AssessmentErrorVO>> assessmentErrorList(@RequestBody AssessmentPageVO assessmentPageVO) {
        IPage<AssessmentErrorVO> page=indicatorReportService.assessmentErrorList(assessmentPageVO);
        return RestResponse.ok(page);
    }

}

