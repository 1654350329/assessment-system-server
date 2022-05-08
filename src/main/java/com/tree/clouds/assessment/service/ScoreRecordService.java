package com.tree.clouds.assessment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.ScoreRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.assessment.model.vo.*;

/**
 * <p>
 * 专家评分记录表 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
public interface ScoreRecordService extends IService<ScoreRecord> {

    void updateScore(UpdateScoreRecord updateScoreRecord);

    IPage<ReviewVO> reviewPage(ReviewPageVO reviewPageVO);

    ScoreRecord getScore(String detailId, String unitId,Integer type);

    void expertReview(UpdateScoreRecord updateScoreRecord);

}
