package com.tree.clouds.assessment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.*;
import com.tree.clouds.assessment.mapper.ComprehensiveAssessmentMapper;
import com.tree.clouds.assessment.model.vo.*;
import com.tree.clouds.assessment.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 综合评定表 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@Service
public class ComprehensiveAssessmentServiceImpl extends ServiceImpl<ComprehensiveAssessmentMapper, ComprehensiveAssessment> implements ComprehensiveAssessmentService {

    @Autowired
    private IndicatorReportService indicatorReportService;
    @Autowired
    private AssessmentConditionService assessmentConditionService;
    @Autowired
    private ScoreRecordService scoreRecordService;
    @Autowired
    private UnitManageService unitManageService;
    @Autowired
    private AssessmentIndicatorsDetailService detailService;
    @Autowired
    private AssessmentIndicatorsService assessmentIndicatorsService;


    @Override
    public IPage<ComprehensiveAssessment> performancePage(PerformancePageVO performancePageVO) {
        return this.baseMapper.performancePage(performancePageVO.getPage(), performancePageVO);
    }

    public ComprehensiveAssessment getAssessmentByUnitAndYear(String unitId, String assessmentYear) {
        return this.getOne(new QueryWrapper<ComprehensiveAssessment>()
                .eq(ComprehensiveAssessment.UNIT_ID, unitId)
                .eq(ComprehensiveAssessment.ASSESSMENT_YEAR, assessmentYear));

    }

    /**
     * @param assessmentVO
     */
    @Override
    @Transactional
    public void assessment(ComprehensiveAssessmentVO assessmentVO) {
        ComprehensiveAssessment comprehensiveAssessment = BeanUtil.toBean(assessmentVO, ComprehensiveAssessment.class);
        comprehensiveAssessment.setComprehensiveProgress(1);
        this.updateById(comprehensiveAssessment);
        ComprehensiveAssessment assessment = this.getById(comprehensiveAssessment);
        List<IndicatorReport> list = indicatorReportService.list(new QueryWrapper<IndicatorReport>().eq(IndicatorReport.REPORT_PROGRESS, 4));
        for (IndicatorReport indicatorReport : list) {
            //修改上报进度
            indicatorReportService.updateProgress(indicatorReport.getReportId(), 5);
        }
        //修改专家评分状态
        scoreRecordService.updateStatusByYearAndUnit(assessment.getAssessmentYear(), assessment.getUnitId());

    }


    @Override
    public void assessmentComplete(AssessmentCompleteVO assessmentCompleteVO) {
        if (StrUtil.isBlank(assessmentCompleteVO.getAssessmentYear())){
            assessmentCompleteVO.setAssessmentYear(String.valueOf(DateUtil.year(new Date())));
        }
        List<AssessmentConditionVO> assessmentConditionVOS = this.getCompleteDate(assessmentCompleteVO.getUnitId(), assessmentCompleteVO.getAssessmentYear());

        //修改状态
        ComprehensiveAssessment assessment = new ComprehensiveAssessment();
        assessment.setComprehensiveProgress(2);
        this.update(assessment, new QueryWrapper<ComprehensiveAssessment>()
                .eq(ComprehensiveAssessment.ASSESSMENT_YEAR, assessmentCompleteVO.getAssessmentYear())
                .eq(ComprehensiveAssessment.UNIT_ID, assessmentCompleteVO.getUnitId()));
        //添加到得分详细说明
        List<AssessmentCondition> assessmentConditions = assessmentConditionVOS.stream().map(vo -> {
            AssessmentCondition assessmentCondition = new AssessmentCondition();
            assessmentCondition.setAssessmentYear(vo.getAssessmentYear());
            UnitManage unitManage = unitManageService.getById(vo.getUnitId());
            assessmentCondition.setUnitName(unitManage.getUnitName());
            assessmentCondition.setIndicatorsName(vo.getIndicatorsName());
            assessmentCondition.setAssessmentCriteria(vo.getAssessmentCriteria());
            //专家评分
            assessmentCondition.setIllustrate(String.valueOf(vo.getIllustrate()));
            return assessmentCondition;
        }).collect(Collectors.toList());
        assessmentConditionService.saveBatch(assessmentConditions);
    }

    private List<AssessmentConditionVO> getCompleteDate(String unitId, String assessmentYear) {
        return this.baseMapper.getCompleteDate(unitId, assessmentYear);

    }

    @Override
    public void reAssessment(ReAssessmentVO reAssessmentVO) {
        //修改专家
        ScoreRecord scoreRecord = scoreRecordService.getByReportId(reAssessmentVO.getId());
        scoreRecord.setScoreType(2);
        scoreRecord.setRemark(reAssessmentVO.getRemark());
        this.scoreRecordService.updateById(scoreRecord);
    }

    @Override
    public ComprehensiveAssessment getByYearAndUnitId(int year, String unitId) {
        return this.getOne(new QueryWrapper<ComprehensiveAssessment>().eq(ComprehensiveAssessment.UNIT_ID, unitId)
                .eq(ComprehensiveAssessment.ASSESSMENT_YEAR, year));
    }
}
