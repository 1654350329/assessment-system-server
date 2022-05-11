package com.tree.clouds.assessment.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.EvaluationEvel;
import com.tree.clouds.assessment.mapper.EvaluationEvelMapper;
import com.tree.clouds.assessment.model.vo.EvaluationEvelVO;
import com.tree.clouds.assessment.service.EvaluationEvelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 评价等级表 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@Service
public class EvaluationEvelServiceImpl extends ServiceImpl<EvaluationEvelMapper, EvaluationEvel> implements EvaluationEvelService {

    @Override
    public IPage<EvaluationEvel> evaluationEvelPage(EvaluationEvelVO pageVO) {
        return this.baseMapper.evaluationEvelPage(pageVO.getPage(),pageVO);
    }

    @Override
    public void deleteEvaluation(List<String> ids) {
        this.baseMapper.deleteEvaluation(ids);
    }
}
