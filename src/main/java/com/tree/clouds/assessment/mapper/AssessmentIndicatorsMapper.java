package com.tree.clouds.assessment.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.AssessmentIndicators;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tree.clouds.assessment.model.vo.AssessmentPageVO;
import com.tree.clouds.assessment.model.vo.AssessmentVO;
import com.tree.clouds.assessment.model.vo.IndicatorsTreeTreeVO;

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

    List<IndicatorsTreeTreeVO> getByYear(Integer year, Integer type, Integer indicatorsType);

    List<IndicatorsTreeTreeVO> getByIndicatorId(String id);

    IPage<AssessmentVO> assessmentPage(IPage<AssessmentVO> page, AssessmentPageVO assessmentPageVO);

    void updateExpirationDate(String assessmentYear, String expirationDate);

    List<AssessmentIndicators> getGroupByYear(String year);

    int getDistributeNumber(String id, Integer type, String unitId);

    List<IndicatorsTreeTreeVO> getByReportId(String id, String unitId, String reportId, Integer reportStatus, String content);

    AssessmentIndicators getByNameAndPid(String indicatorsName, String pid);

    Integer getScoreSumByYear(Integer year, Integer indicatorsType);

    Integer getUnitNumberByYear(String assessmentYear);

    List<IndicatorsTreeTreeVO> getByName(String content);

    IndicatorsTreeTreeVO getIndicatorsTreeTreeVOById(String parentId);

    List<IndicatorsTreeTreeVO> getIndicatorsTreeTreeVOByPId(String id);

    List<IndicatorsTreeTreeVO> getScoreLeftTree(Integer year, String unitId, Integer unitType, Integer progress);

    void removeByYearAndType(Integer assessmentYear, Integer indicatorsType);
}
