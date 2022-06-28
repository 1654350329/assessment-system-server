package com.tree.clouds.assessment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.*;
import com.tree.clouds.assessment.mapper.ComprehensiveAssessmentMapper;
import com.tree.clouds.assessment.model.vo.*;
import com.tree.clouds.assessment.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.assessment.utils.BaseBusinessException;
import com.tree.clouds.assessment.utils.LoginUserUtil;
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
    @Autowired
    private MatterListService matterListService;
    @Autowired
    private RoleManageService roleManageService;


    @Override
    public IPage<ComprehensiveAssessment> performancePage(PerformancePageVO performancePageVO) {
        List<RoleManage> roleManageList = roleManageService.getRoleByUserId(LoginUserUtil.getUserId());
        List<String> role_admin = roleManageList.stream().map(RoleManage::getRoleCode).filter(code -> code.equals("ROLE_admin")).collect(Collectors.toList());
        if (CollUtil.isEmpty(role_admin)) {
            performancePageVO.setUnitId(LoginUserUtil.getUnitId());
            performancePageVO.setComprehensiveProgress("2");
        }
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
        ComprehensiveAssessment assessment = this.getById(comprehensiveAssessment.getComprehensiveId());
        //修改专家评分状态
        scoreRecordService.updateStatusByYearAndUnit(assessment.getAssessmentYear(), assessment.getUnitId(), 1);


    }


    @Override
    @Transactional
    public void assessmentComplete(AssessmentCompleteVO assessmentCompleteVO) {
        if (StrUtil.isBlank(assessmentCompleteVO.getAssessmentYear())) {
            assessmentCompleteVO.setAssessmentYear(String.valueOf(DateUtil.year(new Date())));
        }
        ComprehensiveAssessment one = this.getOne(new QueryWrapper<ComprehensiveAssessment>()
                .eq(ComprehensiveAssessment.ASSESSMENT_YEAR, assessmentCompleteVO.getAssessmentYear())
                .eq(ComprehensiveAssessment.UNIT_ID, assessmentCompleteVO.getUnitId()));
        if (one != null && one.getComprehensiveProgress() == 2) {
            throw new BaseBusinessException(400, "已完成考核!");
        }
        List<AssessmentConditionVO> assessmentConditionVOS = this.getCompleteDate(assessmentCompleteVO.getUnitId(), assessmentCompleteVO.getAssessmentYear());
        List<ScoreRecord> scoreRecords = scoreRecordService.getByTypeAndUnitIdAndYear(assessmentCompleteVO.getUnitId(), assessmentCompleteVO.getAssessmentYear(), 0);
        if (CollUtil.isNotEmpty(scoreRecords)) {
            throw new BaseBusinessException(400, "还有待复评,未评审!");
        }
        scoreRecordService.updateStatusByYearAndUnit(assessmentCompleteVO.getAssessmentYear(), assessmentCompleteVO.getUnitId(), 3);
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
            assessmentCondition.setIllustrate(vo.getIllustrate());
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
        scoreRecord.setExpertStatus(0);
        scoreRecord.setRemark(reAssessmentVO.getRemark());
        this.scoreRecordService.updateById(scoreRecord);
        //添加到待办
        AssessmentIndicatorsDetail detail = this.detailService.getByReportId(reAssessmentVO.getId());
        AssessmentIndicators indicators = this.assessmentIndicatorsService.getById(detail.getProjectId());
        matterListService.addMatter(indicators.getIndicatorsName() + "-" + detail.getAssessmentCriteria() + "材料复评通知", null, reAssessmentVO.getId(), indicators.getUnitId(), 4, indicators.getAssessmentYear(), detail.getIndicatorsId());
    }

    @Override
    public ComprehensiveAssessment getByYearAndUnitId(String year, String unitId) {
        return this.getOne(new QueryWrapper<ComprehensiveAssessment>().eq(ComprehensiveAssessment.UNIT_ID, unitId)
                .eq(ComprehensiveAssessment.ASSESSMENT_YEAR, year));
    }
}
