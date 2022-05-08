package com.tree.clouds.assessment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.ComprehensiveAssessment;
import com.tree.clouds.assessment.mapper.ComprehensiveAssessmentMapper;
import com.tree.clouds.assessment.model.vo.ComprehensiveAssessmentVO;
import com.tree.clouds.assessment.model.vo.PerformancePageVO;
import com.tree.clouds.assessment.model.vo.PerformanceVO;
import com.tree.clouds.assessment.model.vo.ReEvaluationVO;
import com.tree.clouds.assessment.service.ComprehensiveAssessmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.assessment.service.IndicatorReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public IPage<PerformanceVO> performancePage(PerformancePageVO performancePageVO) {
        return this.baseMapper.performancePage(performancePageVO.getPage(), performancePageVO);
    }

    @Override
    public void assessment(ComprehensiveAssessmentVO assessmentVO) {
        ComprehensiveAssessment comprehensiveAssessment = BeanUtil.toBean(assessmentVO, ComprehensiveAssessment.class);
        comprehensiveAssessment.setComprehensiveProgress(1);
        this.updateById(comprehensiveAssessment);
    }

    @Override
    public void reEvaluation(ReEvaluationVO evaluationVO) {
        ComprehensiveAssessment assessment = this.getById(evaluationVO.getComprehensiveId());
        if (evaluationVO.getComprehensiveStatus()==2){

        }else {
            assessment.setComprehensiveProgress(2);
        }
    }
}
