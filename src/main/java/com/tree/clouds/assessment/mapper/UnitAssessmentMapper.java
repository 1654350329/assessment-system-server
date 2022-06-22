package com.tree.clouds.assessment.mapper;

import com.tree.clouds.assessment.model.entity.UnitAssessment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tree.clouds.assessment.model.vo.UnitVO;

import java.util.List;

/**
 * <p>
 * 指标单位中间表 Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
public interface UnitAssessmentMapper extends BaseMapper<UnitAssessment> {

    int getCount(String assessmentYear);

    Integer getDistributeNumber(String unitId, Integer type);

    Integer getCountUnit(String unitId, Integer year);

    List<UnitAssessment> getByYear(String assessmentYear);

    Integer isRelease(Integer year);

    Integer getExpertDistributeNumber(String unitId, String expertUnit);

    Double getScoreByUnitIdAndYear(String unitId, String year);
}
