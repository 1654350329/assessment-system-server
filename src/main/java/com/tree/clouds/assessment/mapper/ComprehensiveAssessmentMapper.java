package com.tree.clouds.assessment.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.ComprehensiveAssessment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tree.clouds.assessment.model.vo.PerformancePageVO;
import com.tree.clouds.assessment.model.vo.PerformanceVO;

/**
 * <p>
 * 综合评定表 Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
public interface ComprehensiveAssessmentMapper extends BaseMapper<ComprehensiveAssessment> {

    IPage<PerformanceVO> performancePage(IPage<PerformanceVO> page, PerformancePageVO performancePageVO);

}
