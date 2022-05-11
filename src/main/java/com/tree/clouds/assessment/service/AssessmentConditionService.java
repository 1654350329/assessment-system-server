package com.tree.clouds.assessment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.assessment.model.entity.AssessmentCondition;
import com.tree.clouds.assessment.model.vo.ConditionVO;
import com.tree.clouds.assessment.model.vo.GetConditionVO;
import com.tree.clouds.assessment.model.vo.ScorePageVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 考核情况表 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-05-08
 */
public interface AssessmentConditionService extends IService<AssessmentCondition> {

    IPage<ConditionVO> conditionPage(ScorePageVO scorePageVO);

    List<AssessmentCondition> getConditionList(GetConditionVO getConditionVO);

    void exportDate(GetConditionVO getConditionVO, HttpServletResponse response);
}
