package com.tree.clouds.assessment.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.common.RestResponse;
import com.tree.clouds.assessment.common.aop.Log;
import com.tree.clouds.assessment.model.entity.RatingRecordHistory;
import com.tree.clouds.assessment.model.entity.ScoreRecord;
import com.tree.clouds.assessment.model.vo.*;
import com.tree.clouds.assessment.service.IndicatorReportService;
import com.tree.clouds.assessment.service.RatingRecordHistoryService;
import com.tree.clouds.assessment.service.ScoreRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    @Autowired
    private RatingRecordHistoryService ratingRecordHistoryService;

    @Log("评分列表分页")
    @PostMapping("/scorePage")
    @ApiOperation(value = "评分列表分页")
    public RestResponse<IPage<IndicatorReportVO>> scorePage(@RequestBody AssessmentPageVO assessmentPageVO) {
        IPage<IndicatorReportVO> iPage = indicatorReportService.assessmentList(assessmentPageVO, 1);
        return RestResponse.ok(iPage);
    }

    @Log("专家评分复评初评树")
    @PostMapping("/scoreTree")
    @ApiOperation(value = "专家评分复评初评树")
    public RestResponse<List<indicatorsTreeTreeVO>> scoreTree(@RequestBody AuditTreeVO auditTreeVO) {
        List<indicatorsTreeTreeVO> tree = scoreRecordService.scoreTree(auditTreeVO);
        return RestResponse.ok(tree);
    }

//    @Log("获取各个分值")
//    @PostMapping("/getScore")
//    @ApiOperation(value = "获取各个分值")
//    public RestResponse<Map<String,Integer>> getScore() {
////        List<indicatorsTreeTreeVO> tree = scoreRecordService.getScore(auditTreeVO);
//        return RestResponse.ok(null);
//    }

    @Log("专家评分")
    @PostMapping("/updateScore")
    @ApiOperation(value = "专家评分")
    public RestResponse<Boolean> updateScore(@RequestBody UpdateScoreRecord updateScoreRecord) {
        scoreRecordService.updateScore(updateScoreRecord);
        return RestResponse.ok(true);
    }

    @Log("获取专家评分")
    @PostMapping("/getScore")
    @ApiOperation(value = "获取专家评分")
    public RestResponse<ScoreRecord> getScore(@RequestBody GetScoreVO getScoreVO) {
        ScoreRecord scoreRecord= scoreRecordService.getScore(getScoreVO.getId(),getScoreVO.getUnitId(),getScoreVO.getScoreType());
        return RestResponse.ok(scoreRecord);
    }
    @Log("复评列表")
    @PostMapping("/reviewPage")
    @ApiOperation(value = "复评列表")
    public RestResponse<IPage<ReviewVO>> reviewPage(@RequestBody ReviewPageVO reviewPageVO) {
        IPage<ReviewVO> page= scoreRecordService.reviewPage(reviewPageVO);
        return RestResponse.ok(page);
    }

    @Log("获取评分历史 ")
    @PostMapping("/getHistoryList")
    @ApiOperation(value = "获取评分历史")
    public RestResponse<List<RatingRecordHistory>> getHistoryList(@RequestBody PublicIdReqVO publicIdReqVO) {
        List<RatingRecordHistory> historyList = ratingRecordHistoryService.getHistoryList(publicIdReqVO.getId());
        return RestResponse.ok(historyList);
    }


}

