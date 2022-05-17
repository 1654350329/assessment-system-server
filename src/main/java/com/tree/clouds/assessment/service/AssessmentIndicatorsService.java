package com.tree.clouds.assessment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.AssessmentIndicators;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.assessment.model.vo.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 考核指标配置 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
public interface AssessmentIndicatorsService extends IService<AssessmentIndicators> {

    List<IndicatorsTreeTreeVO> indicatorsTree(Integer year, Integer type);

    List<IndicatorsTreeTreeVO> indicatorsChildrenTree(String id);

    List<IndicatorsTreeTreeVO> getByReportId(String id, String unitId, String reportId, Integer indicatorsStatus, String content);

    AssessmentIndicators evaluationStandard(String id);

    void addTask(UpdateTaskVO updateTaskVO);

    void deleteIndicators(List<String> ids);

    void updateIndicators(UpdateIndicatorsVO updateIndicatorsVO);

    void addIndicators(UpdateIndicatorsVO updateIndicatorsVO);

    void updateTask(UpdateTaskVO updateTaskVO);

    void releaseAssessment(ReleaseAssessmentVO releaseAssessmentVO);

    IPage<AssessmentVO> assessmentPage(AssessmentPageVO assessmentPageVO);

    void updateExpirationDate(String assessmentYear, String expirationDate);

    List<IndicatorsTreeTreeVO> getTreeById(String id);

    List<AssessmentIndicators> getGroupByYear(String year);

    List<IndicatorsTreeTreeVO> auditTree(AuditTreeVO auditTreeVO);

    void copyTask(Integer year);

    int getScoreSum(Integer year);

    void export(Integer year ,HttpServletResponse response);
}
