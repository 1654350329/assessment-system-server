package com.tree.clouds.assessment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tree.clouds.assessment.model.entity.AssessmentIndicatorsDetail;

/**
 * <p>
 * 考核指标详细表 Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-05-08
 */
public interface AssessmentIndicatorsDetailMapper extends BaseMapper<AssessmentIndicatorsDetail> {

    Double getScoreByType(String id, Integer type,String year);

    Double getScoreByUnit(String unitId,String id);

    int getCountByType(String id, Integer type,String year);
}
