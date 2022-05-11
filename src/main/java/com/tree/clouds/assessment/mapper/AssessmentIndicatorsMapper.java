package com.tree.clouds.assessment.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.AssessmentIndicators;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tree.clouds.assessment.model.vo.AssessmentPageVO;
import com.tree.clouds.assessment.model.vo.AssessmentVO;
import com.tree.clouds.assessment.model.vo.indicatorsTreeTreeVO;

import java.util.List;

/**
 * <p>
 * 考核指标配置 Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
public interface AssessmentIndicatorsMapper extends BaseMapper<AssessmentIndicators> {

    List<indicatorsTreeTreeVO> getByYear(Integer year,Integer type);

    List<indicatorsTreeTreeVO> getByIndicatorId(String id);

    IPage<AssessmentVO> assessmentPage(IPage<AssessmentVO> page, AssessmentPageVO assessmentPageVO);

    void updateExpirationDate(String assessmentYear, String expirationDate);

    List<AssessmentIndicators> getGroupByYear(String year);

    int getDistributeNumber(String id, Integer type);

    List<indicatorsTreeTreeVO> getByReportId(String id, String unitId, String reportId, Integer reportStatus);

    AssessmentIndicators getByName(String indicatorsName,Integer year);
}
