package com.tree.clouds.assessment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.assessment.mapper.RatingRecordHistoryMapper;
import com.tree.clouds.assessment.model.entity.RatingRecordHistory;
import com.tree.clouds.assessment.service.RatingRecordHistoryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评分历史记录 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-05-08
 */
@Service
public class RatingRecordHistoryServiceImpl extends ServiceImpl<RatingRecordHistoryMapper, RatingRecordHistory> implements RatingRecordHistoryService {

    /**
     * 添加评分历史
     * @param detailId
     * @param expertScore
     * @param illustrate
     */
    @Override
    public void addRecord(String detailId, Double expertScore, String illustrate) {
        RatingRecordHistory ratingRecordHistory = new RatingRecordHistory();
        ratingRecordHistory.setDetailId(detailId);
        ratingRecordHistory.setExpertScore(expertScore);
        ratingRecordHistory.setIllustrate(illustrate);
        this.save(ratingRecordHistory);
    }
}
