package com.tree.clouds.assessment.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.ScoreRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tree.clouds.assessment.model.vo.ReviewPageVO;
import com.tree.clouds.assessment.model.vo.ReviewVO;

import java.util.List;

/**
 * <p>
 * 评分记录表 Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
public interface ScoreRecordMapper extends BaseMapper<ScoreRecord> {

    Double getExpertRating(String unitId, String assessmentYear);

    IPage<ReviewVO> getReviewPage(IPage<ReviewVO> page, ReviewPageVO reviewPageVO);

    Double getSumByUnitAndYear(String unitId, int year);

    void updateStatusByYearAndUnit(String assessmentYear, String unitId,Integer status);

    List<ScoreRecord> getByTypeAndUnitIdAndYear(String assessmentYear, String unitId, Integer type);
}
