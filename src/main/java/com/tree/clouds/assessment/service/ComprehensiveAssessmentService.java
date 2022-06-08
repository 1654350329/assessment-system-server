package com.tree.clouds.assessment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.ComprehensiveAssessment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.assessment.model.vo.*;

import java.util.List;

/**
 * <p>
 * 综合评定表 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
public interface ComprehensiveAssessmentService extends IService<ComprehensiveAssessment> {

    IPage<ComprehensiveAssessment> performancePage(PerformancePageVO performancePageVO);

    void assessment(ComprehensiveAssessmentVO assessmentVO);


    ComprehensiveAssessment getAssessmentByUnitAndYear(String unitId, String assessmentYear);


    void assessmentComplete(AssessmentCompleteVO assessmentCompleteVO);


    void reAssessment(ReAssessmentVO reAssessmentVO);

    ComprehensiveAssessment getByYearAndUnitId(String year, String unitId);
}
