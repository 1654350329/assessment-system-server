package com.tree.clouds.assessment.service;

import com.tree.clouds.assessment.model.entity.UnitAssessment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.assessment.model.vo.UnitVO;

import java.util.List;

/**
 * <p>
 * 指标单位中间表 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
public interface UnitAssessmentService extends IService<UnitAssessment> {

    List<UnitVO> assessmentList(UnitVO unitVO);

    void addAssessment(List<String> ids,String unitId);

    List<String> getAssessment(String unitId);

    int getCount(String assessmentYear);

    Integer getDistributeNumber(String unitId);


    List<UnitAssessment> getByYear(String assessmentYear);

    Integer getExpertDistributeNumber(String unitId, String expertUnit);
    Double getScoreByUnitIdAndYear(String unitId, String year);
}
