package com.tree.clouds.assessment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.EvaluationEvel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.assessment.model.vo.EvaluationEvelVO;

import java.util.List;

/**
 * <p>
 * 评价等级表 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
public interface EvaluationEvelService extends IService<EvaluationEvel> {

    IPage<EvaluationEvel> evaluationEvelPage(EvaluationEvelVO pageVO);

    void deleteEvaluation(List<String> ids);
}
