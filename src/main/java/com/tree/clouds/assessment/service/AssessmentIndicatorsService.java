package com.tree.clouds.assessment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.AssessmentIndicators;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.assessment.model.vo.*;

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

    List<indicatorsTreeTreeVO> indicatorsTree(Integer year,Integer type);

    List<indicatorsTreeTreeVO> indicatorsChildrenTree(String id);

    AssessmentIndicators evaluationStandard(String id);

    void addTask(UpdateTaskVO updateTaskVO);

    void deleteIndicators(List<String> ids);

    void updateIndicators(UpdateIndicatorsVO updateIndicatorsVO);

    void addIndicators(UpdateIndicatorsVO updateIndicatorsVO);

    void updateTask(UpdateTaskVO updateTaskVO);

    void releaseAssessment(ReleaseAssessmentVO releaseAssessmentVO);

    IPage<AssessmentVO> assessmentPage(AssessmentPageVO assessmentPageVO);

    void updateExpirationDate(String assessmentYear, String expirationDate);

}
