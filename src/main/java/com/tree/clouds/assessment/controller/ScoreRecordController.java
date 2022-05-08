package com.tree.clouds.assessment.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.common.RestResponse;
import com.tree.clouds.assessment.common.aop.Log;
import com.tree.clouds.assessment.model.entity.ScoreRecord;
import com.tree.clouds.assessment.model.vo.*;
import com.tree.clouds.assessment.service.IndicatorReportService;
import com.tree.clouds.assessment.service.ScoreRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 评分记录表 前端控制器
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@RestController
@RequestMapping("/score-record")
@Api(tags = "专家评分管理模块")
public class ScoreRecordController {
    @Autowired
    private ScoreRecordService scoreRecordService;
    @Autowired
    private IndicatorReportService indicatorReportService;

    @Log("评分列表分页")
    @PostMapping("/scorePage")
    @ApiOperation(value = "评分列表分页")
    @PreAuthorize("hasAuthority('user:manage:list')")
    public RestResponse<IPage<IndicatorReportVO>> scorePage(@RequestBody AssessmentPageVO assessmentPageVO) {
        IPage<IndicatorReportVO> iPage = indicatorReportService.assessmentList(assessmentPageVO, 1);
        return RestResponse.ok(iPage);
    }

    @Log("专家评分")
    @PostMapping("/updateScore")
    @ApiOperation(value = "专家评分")
    @PreAuthorize("hasAuthority('user:manage:list')")
    public RestResponse<Boolean> updateScore(@RequestBody UpdateScoreRecord updateScoreRecord) {
        scoreRecordService.updateScore(updateScoreRecord);
        return RestResponse.ok(true);
    }

    @Log("获取专家评分")
    @PostMapping("/getScore")
    @ApiOperation(value = "获取专家评分")
    @PreAuthorize("hasAuthority('user:manage:list')")
    public RestResponse<ScoreRecord> getScore(@RequestBody GetScoreVO getScoreVO) {
        ScoreRecord scoreRecord= scoreRecordService.getScore(getScoreVO.getId(),getScoreVO.getUnitId(),getScoreVO.getScoreType());
        return RestResponse.ok(scoreRecord);
    }

    @Log("复评列表")
    @PostMapping("/reviewPage")
    @ApiOperation(value = "复评列表")
    @PreAuthorize("hasAuthority('user:manage:list')")
    public RestResponse<IPage<ReviewVO>> reviewPage(@RequestBody ReviewPageVO reviewPageVO) {
        IPage<ReviewVO> page= scoreRecordService.reviewPage(reviewPageVO);
        return RestResponse.ok(page);
    }

    @Log("专家复评评分")
    @PostMapping("/expertReview")
    @ApiOperation(value = "专家复评评分")
    @PreAuthorize("hasAuthority('user:manage:list')")
    public RestResponse<Boolean> expertReview(@RequestBody UpdateScoreRecord updateScoreRecord) {
        scoreRecordService.expertReview(updateScoreRecord);
        return RestResponse.ok(true);
    }
}

