package com.tree.clouds.assessment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.ScoreRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.assessment.model.vo.*;

import java.util.List;

/**
 * <p>
 * 专家评分记录表 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
public interface ScoreRecordService extends IService<ScoreRecord> {

    void updateScore(UpdateScoreRecord updateScoreRecord);

    IPage<ReviewVO> reviewPage(ReviewPageVO reviewPageVO);

    ScoreRecord getScore(String detailId, String unitId, Integer type);

    void isCompleteResult(String unitId, String year);

    double getExpertRating(String unitId, String assessmentYear);

    List<IndicatorsTreeTreeVO> scoreTree(AuditTreeVO auditTreeVO);

    ScoreRecord getByReportId(String id);

    void updateStatusByYearAndUnit(String assessmentYear, String unitId,Integer type);

    List<ScoreRecord> getByTypeAndUnitIdAndYear(String unitId, String assessmentYear,Integer type);
}
