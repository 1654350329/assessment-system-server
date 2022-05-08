package com.tree.clouds.assessment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.*;
import com.tree.clouds.assessment.mapper.ScoreRecordMapper;
import com.tree.clouds.assessment.model.vo.*;
import com.tree.clouds.assessment.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public void updateScore(UpdateScoreRecord updateScoreRecord) {
        this.remove(new QueryWrapper<ScoreRecord>()
                .eq(ScoreRecord.DETAIL_ID, updateScoreRecord.getId())
                .eq(ScoreRecord.UNIT_ID, updateScoreRecord.getUnitId())
        .eq(ScoreRecord.SCORE_TYPE,0));
        ScoreRecord scoreRecord = BeanUtil.toBean(updateScoreRecord, ScoreRecord.class);
        scoreRecord.setScoreType(0);
        this.save(scoreRecord);
        //更新上报进度
        IndicatorReport indicatorReport = indicatorReportService.getByDetailIdAndUnitId(updateScoreRecord.getId(), updateScoreRecord.getUnitId());
        indicatorReportService.updateProgress(indicatorReport.getReportId(),2);
        //添加评分历史
        ratingRecordHistoryService.addRecord(scoreRecord.getDetailId(),scoreRecord.getExpertScore(),scoreRecord.getIllustrate());
    }

    @Override
    public IPage<ReviewVO> reviewPage(ReviewPageVO reviewPageVO) {
        IPage<ReviewVO> page = reviewPageVO.getPage();
        List<IndicatorReport> list = indicatorReportService.list(new QueryWrapper<IndicatorReport>().eq(IndicatorReport.REPORT_PROGRESS, 3));
        List<ReviewVO> collect = list.stream().map(indicatorReport -> {
            AssessmentIndicators indicators = assessmentIndicatorsService.getById(indicatorReport.getIndicatorsId());
            ReviewVO reviewVO = BeanUtil.toBean(indicators, ReviewVO.class);
            UserManage userManage = userManageService.getById(indicatorReport.getCreatedUser());
            reviewVO.setCreatedUser(userManage.getUserName());
            reviewVO.setPhoneNumber(userManage.getPhoneNumber());
            UnitManage unitManage = unitManageService.getById(userManage.getUnitId());
            reviewVO.setUnitName(unitManage.getUnitName());
            return reviewVO;
        }).collect(Collectors.toList());
        int cursor = Math.toIntExact(((page.getCurrent() - 1) * page.getSize()));
        int limit = Math.toIntExact(page.getSize());
        paging(cursor, limit, collect);

        page.setRecords(collect);
        page.setTotal(collect.size());
        page.setCurrent(reviewPageVO.getCurrent());
        page.setSize(reviewPageVO.getSize());
        return page;
    }

    //手动实现分页
    public List<ReviewVO> paging(int cursor, int limit, List<ReviewVO> list) {
        //手动实现分页
        if (cursor < 0 || cursor >= list.size() || limit <= 0) {
            return null;
        }
        int lastIndex = cursor + limit;
        if (lastIndex > list.size()) {
            lastIndex = list.size();
        }
        //获得分页后的deviceIdList
        list = list.subList(cursor, lastIndex);
        return list;
    }

    /**
     * 获取专家评分
     * @param indicatorsId
     * @param unitId
     * @param type 0初评 1复评
     * @return
     */
    @Override
    public ScoreRecord getScore(String indicatorsId, String unitId,Integer type) {
        return  this.getOne(new QueryWrapper<ScoreRecord>().eq(ScoreRecord.DETAIL_ID,indicatorsId)
                .eq(ScoreRecord.UNIT_ID,unitId)
        .eq(ScoreRecord.SCORE_TYPE,type));
    }

    @Override
    public void expertReview(UpdateScoreRecord updateScoreRecord) {
        this.remove(new QueryWrapper<ScoreRecord>()
                .eq(ScoreRecord.DETAIL_ID, updateScoreRecord.getId())
                .eq(ScoreRecord.UNIT_ID, updateScoreRecord.getUnitId())
                .eq(ScoreRecord.SCORE_TYPE,1));
        ScoreRecord scoreRecord = BeanUtil.toBean(updateScoreRecord, ScoreRecord.class);
        scoreRecord.setScoreType(1);
        this.save(scoreRecord);
        //添加评分历史
        ratingRecordHistoryService.addRecord(scoreRecord.getDetailId(),scoreRecord.getExpertScore(),scoreRecord.getIllustrate());

        IndicatorReport indicatorReport = indicatorReportService.getByDetailIdAndUnitId(updateScoreRecord.getId(), updateScoreRecord.getUnitId());
       //更新上报进度
        indicatorReportService.updateProgress(indicatorReport.getReportId(),3);
        //添加到综合评定
        ComprehensiveAssessment comprehensiveAssessment = new ComprehensiveAssessment();
        AssessmentIndicators indicators = assessmentIndicatorsService.getById(indicatorReport.getIndicatorsId());
        UnitManage unitManage = unitManageService.getById( updateScoreRecord.getUnitId());
        //考评年份
        comprehensiveAssessment.setAssessmentYear(indicators.getAssessmentYear());
        //责任单位
        comprehensiveAssessment.setUnitId(unitManage.getUnitId());
        //各项任务总分值
        double score = detailService.getScoreByUnit(unitManage.getUnitId(), indicatorReport.getIndicatorsId());
        if (indicators.getIndicatorsName().equals("县（市、区）绩效任务")){
            comprehensiveAssessment.setTaskScore(score);
        }
        if (indicators.getIndicatorsName().equals("机制创新")){
            comprehensiveAssessment.setInnovationScoreSum(score);
        }
        if (indicators.getIndicatorsName().equals("正向激励加分")){
            comprehensiveAssessment.setPositiveIncentiveSum(score);
        }
        if (indicators.getIndicatorsName().equals("绩效减分")){
            comprehensiveAssessment.setPerformanceScore(score);
        }
        //专家复评分数
        comprehensiveAssessment.setExpertRating(scoreRecord.getExpertScore());
        assessmentService.save(comprehensiveAssessment);
    }
}
