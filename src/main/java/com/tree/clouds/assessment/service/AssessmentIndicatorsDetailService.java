package com.tree.clouds.assessment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.assessment.model.entity.AssessmentIndicatorsDetail;

/**
 * <p>
 * 考核指标详细表 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-05-08
 */
public interface AssessmentIndicatorsDetailService extends IService<AssessmentIndicatorsDetail> {

    double getScoreByType(String id, Integer type,String year);

    double getScoreByUnit(String unitId, String id);

    int getCountByType(String id, Integer assessmentType);
}
