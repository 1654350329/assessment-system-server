package com.tree.clouds.assessment.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.EvaluationEvel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tree.clouds.assessment.model.vo.EvaluationEvelVO;

import java.util.List;

/**
 * <p>
 * 评价等级表 Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
public interface EvaluationEvelMapper extends BaseMapper<EvaluationEvel> {

    IPage<EvaluationEvel> evaluationEvelPage(IPage<EvaluationEvel> page, EvaluationEvelVO pageVO);

    void deleteEvaluation(List<String> ids);
}
