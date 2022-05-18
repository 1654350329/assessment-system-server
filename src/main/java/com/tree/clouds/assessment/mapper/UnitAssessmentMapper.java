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

    Integer getDistributeNumber(String unitId);

    Integer getCountUnit(String unitId, Integer year);

    List<UnitAssessment> getByYear(String assessmentYear);

    Integer isRelease(String detailId);

    Integer getExpertDistributeNumber(String unitId, String userId);
}
