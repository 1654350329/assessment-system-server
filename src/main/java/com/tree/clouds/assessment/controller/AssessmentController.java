package com.tree.clouds.assessment.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.common.RestResponse;
import com.tree.clouds.assessment.common.aop.Log;
import com.tree.clouds.assessment.model.vo.AssessmentPageVO;
import com.tree.clouds.assessment.model.vo.AssessmentVO;
import com.tree.clouds.assessment.model.vo.ReleaseAssessmentVO;
import com.tree.clouds.assessment.service.AssessmentIndicatorsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 考核指标列表 前端控制器
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@RestController
@RequestMapping("/assessment")
@Api(value = "assessment", tags = "考核指标列表模块")
public class AssessmentController {
    @Autowired
    private AssessmentIndicatorsService assessmentIndicatorsService;

    @Log("考核指标列表分页查询")
    @PostMapping("/assessmentPage")
    @ApiOperation(value = "考核指标列表分页查询")
    @PreAuthorize("hasRole('ROLE_admin')")
    public RestResponse<IPage<AssessmentVO>> assessmentPage(@RequestBody AssessmentPageVO assessmentPageVO) {
        IPage<AssessmentVO> page = assessmentIndicatorsService.assessmentPage(assessmentPageVO);
        return RestResponse.ok(page);
    }

    @Log("发布考核指标")
    @PostMapping("/releaseAssessment")
    @ApiOperation(value = "发布考核指标")
    @PreAuthorize("hasRole('ROLE_admin')")
    public RestResponse<Boolean> releaseAssessment(@RequestBody ReleaseAssessmentVO releaseAssessmentVO) {
        if (StrUtil.isBlank(releaseAssessmentVO.getAssessmentYear())) {
            releaseAssessmentVO.setAssessmentYear(String.valueOf(DateUtil.year(new Date())));
        }
        assessmentIndicatorsService.releaseAssessment(releaseAssessmentVO);
        return RestResponse.ok(true);
    }
}
