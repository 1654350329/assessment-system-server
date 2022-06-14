package com.tree.clouds.assessment.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.AssessmentIndicators;
import com.tree.clouds.assessment.model.entity.IndicatorReport;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tree.clouds.assessment.model.vo.AssessmentListVO;
import com.tree.clouds.assessment.model.vo.PageParam;
import com.tree.clouds.assessment.model.vo.PerformancePageVO;
import com.tree.clouds.assessment.model.vo.PerformanceVO;

import java.util.List;

/**
 * <p>
 * 数据上报 Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
public interface IndicatorReportMapper extends BaseMapper<IndicatorReport> {

    Integer getSubmitNumber(String unitId);

    Integer getStatusNumber(String unitId, int status, Integer progress);

    IPage<IndicatorReport> getErrorList(IPage<IndicatorReport> page, String unitId);

    IPage<IndicatorReport> getListByType(IPage<PerformanceVO> page, PerformancePageVO performancePageVO);

    Double getUserScoreByUnit(String unitId, String assessmentYear);

    Integer getMaterial(Integer type, String unitId);

    Integer getMaterial(String unitId, Integer type);

    List<AssessmentListVO> getAssessmentList(String unitId);

    Integer getReviewedNumber(String unitId, int type, String year, String expertUnitId);

    Integer getDistributeSum(String unitId, String year);

    AssessmentIndicators getAssessmentIndicatorsByReportId(String reportId);

    Integer getAuditNumber(String unitId, int year);
}
