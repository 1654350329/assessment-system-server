package com.tree.clouds.assessment.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.ComprehensiveAssessment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tree.clouds.assessment.model.vo.AssessmentConditionVO;
import com.tree.clouds.assessment.model.vo.PerformancePageVO;
import com.tree.clouds.assessment.model.vo.PerformanceVO;

import java.util.List;

/**
 * <p>
 * 综合评定表 Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
public interface ComprehensiveAssessmentMapper extends BaseMapper<ComprehensiveAssessment> {

    IPage<ComprehensiveAssessment> performancePage(IPage<PerformanceVO> page, PerformancePageVO performancePageVO);

    List<AssessmentConditionVO> getCompleteDate(String unitId, String assessmentYear);
}
