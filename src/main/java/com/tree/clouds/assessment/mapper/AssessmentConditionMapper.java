package com.tree.clouds.assessment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.AssessmentCondition;
import com.tree.clouds.assessment.model.vo.ConditionVO;
import com.tree.clouds.assessment.model.vo.ScorePageVO;

/**
 * <p>
 * 考核情况表 Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-05-08
 */
public interface AssessmentConditionMapper extends BaseMapper<AssessmentCondition> {

    IPage<ConditionVO> conditionPage(IPage<ConditionVO> page, ScorePageVO scorePageVO);
}
