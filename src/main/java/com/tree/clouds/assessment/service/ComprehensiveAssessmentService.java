package com.tree.clouds.assessment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.ComprehensiveAssessment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.assessment.model.vo.ComprehensiveAssessmentVO;
import com.tree.clouds.assessment.model.vo.PerformancePageVO;
import com.tree.clouds.assessment.model.vo.PerformanceVO;
import com.tree.clouds.assessment.model.vo.ReEvaluationVO;

/**
 * <p>
 * 综合评定表 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
public interface ComprehensiveAssessmentService extends IService<ComprehensiveAssessment> {

    IPage<PerformanceVO> performancePage(PerformancePageVO performancePageVO);

    void assessment(ComprehensiveAssessmentVO assessmentVO);

    void reEvaluation(ReEvaluationVO evaluationVO);

}
