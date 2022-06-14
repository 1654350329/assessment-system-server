package com.tree.clouds.assessment.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.*;
import com.tree.clouds.assessment.mapper.ScoreRecordMapper;
import com.tree.clouds.assessment.model.vo.*;
import com.tree.clouds.assessment.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.assessment.utils.BaseBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 评分记录表 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@Service
public class ScoreRecordServiceImpl extends ServiceImpl<ScoreRecordMapper, ScoreRecord> implements ScoreRecordService {

    @Autowired
    private IndicatorReportService indicatorReportService;
    @Autowired
    private AssessmentIndicatorsService assessmentIndicatorsService;
    @Autowired
    private UserManageService userManageService;
    @Autowired
    private UnitManageService unitManageService;
    @Autowired
    private ComprehensiveAssessmentService assessmentService;
    @Autowired
    private AssessmentIndicatorsDetailService detailService;
    @Autowired
    private RatingRecordHistoryService ratingRecordHistoryService;
    @Autowired
    private ScoreRecordService scoreRecordService;
    @Autowired
    private ComprehensiveAssessmentService comprehensiveAssessmentService;
    @Autowired
    private UnitAssessmentService unitAssessmentService;


    @Override
    @Transactional
    public void updateScore(UpdateScoreRecord updateScoreRecord) {
        AssessmentIndicatorsDetail detail = detailService.getByReportId(updateScoreRecord.getId());
        if (updateScoreRecord.getExpertScore() > detail.getFraction()) {
            throw new BaseBusinessException(400, "评分不能超过当前考核标准分数!");
        }
        AssessmentIndicators indicators = assessmentIndicatorsService.getById(detail.getIndicatorsId());
        ScoreRecord one = this.getOne(new QueryWrapper<ScoreRecord>().eq(ScoreRecord.REPORT_ID, updateScoreRecord.getId()));
        if (one == null) {
            one = new ScoreRecord();
        }
        one.setReportId(updateScoreRecord.getId());
        one.setIllustrate(updateScoreRecord.getIllustrate());
        one.setExpertScore(updateScoreRecord.getExpertScore());
        one.setExpertStatus(1);
        one.setScoreType(1);
        one.setRemark(null);
        this.saveOrUpdate(one, new QueryWrapper<ScoreRecord>().eq(ScoreRecord.REPORT_ID, updateScoreRecord.getId()));
        //更新上报进度
        IndicatorReport indicatorReport = indicatorReportService.getByReportIdAndUnitId(updateScoreRecord.getId(), null);
        indicatorReportService.updateProgress(indicatorReport.getReportId(), 3);
        //添加评分历史
        ratingRecordHistoryService.addRecord(one.getReportId(), one.getExpertScore(), one.getIllustrate());
        //添加到成绩管理
        ComprehensiveAssessment byYearAndUnitId = this.comprehensiveAssessmentService.getByYearAndUnitId(indicators.getAssessmentYear(), indicatorReport.getUnitId());
        if (byYearAndUnitId == null && isComplete(updateScoreRecord.getUnitId(), indicators.getAssessmentYear())) {
            UnitManage unitManage = unitManageService.getById(updateScoreRecord.getUnitId());
            ComprehensiveAssessment assessment = new ComprehensiveAssessment();
            assessment.setUnitId(updateScoreRecord.getUnitId());
            assessment.setComprehensiveProgress(0);//初审为0
            assessment.setIndicatorsType(unitManage.getUnitType());
            assessment.setAssessmentYear(indicators.getAssessmentYear());
            Double sum = this.baseMapper.getExpertRating(updateScoreRecord.getUnitId(), indicators.getAssessmentYear());
            assessment.setOnLineScore(sum.toString());
            Double score = unitAssessmentService.getScoreByUnitIdAndYear(updateScoreRecord.getUnitId(), indicators.getAssessmentYear());
            assessment.setTaskScore(score.toString());
            comprehensiveAssessmentService.save(assessment);
        }
        if (byYearAndUnitId != null) {
            Double sum = this.baseMapper.getExpertRating(indicatorReport.getUnitId(), byYearAndUnitId.getAssessmentYear());
            byYearAndUnitId.setOnLineScore(String.valueOf(sum));
            comprehensiveAssessmentService.updateById(byYearAndUnitId);
        }

    }

    public Boolean isComplete(String unitId, String year) {
        Integer reviewedNumber = this.indicatorReportService.getReviewedNumber(unitId, 2, year);
        Integer distributeNumber = unitAssessmentService.getDistributeNumber(unitId);
        return reviewedNumber.equals(distributeNumber);
    }

    @Override
    public IPage<ReviewVO> reviewPage(ReviewPageVO reviewPageVO) {
        IPage<ReviewVO> page = reviewPageVO.getPage();
        return this.baseMapper.getReviewPage(page, reviewPageVO);
    }


    /**
     * 获取专家评分
     *
     * @param indicatorsId
     * @param unitId
     * @param type         0初评 1复评
     * @return
     */
    @Override
    public ScoreRecord getScore(String indicatorsId, String unitId, Integer type) {
        return this.getOne(new QueryWrapper<ScoreRecord>().eq(ScoreRecord.DETAIL_ID, indicatorsId)
                .eq(ScoreRecord.UNIT_ID, unitId)
                .eq(ScoreRecord.SCORE_TYPE, type));
    }


    @Override
    public double getExpertRating(String unitId, String assessmentYear) {
        Double score = this.baseMapper.getExpertRating(unitId, assessmentYear);
        return score == null ? 0 : score;
    }

    @Override
    public List<IndicatorsTreeTreeVO> scoreTree(AuditTreeVO auditTreeVO) {
        int status = 2;
        if (StrUtil.isNotBlank(auditTreeVO.getReportStatus())) {
            //0未评 1已评 对应上报进度 2 跟3
            int i = Integer.parseInt(auditTreeVO.getReportStatus());
            status = i + status;
        }
        return this.indicatorReportService.getTreeById(auditTreeVO.getId(), auditTreeVO.getUnitId(), auditTreeVO.getReportId(), status, auditTreeVO.getContent(), 3);
    }

    @Override
    public ScoreRecord getByReportId(String id) {
        return this.getOne(new QueryWrapper<ScoreRecord>().eq(ScoreRecord.REPORT_ID, id));
    }

    @Override
    public void updateStatusByYearAndUnit(String assessmentYear, String unitId, Integer type) {
        this.baseMapper.updateStatusByYearAndUnit(assessmentYear, unitId, type);
    }

    @Override
    public List<ScoreRecord> getByTypeAndUnitIdAndYear(String unitId, String assessmentYear, Integer type) {
        return this.baseMapper.getByTypeAndUnitIdAndYear(assessmentYear, unitId, type);
    }
}
