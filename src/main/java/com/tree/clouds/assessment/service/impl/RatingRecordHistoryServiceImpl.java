package com.tree.clouds.assessment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.assessment.mapper.RatingRecordHistoryMapper;
import com.tree.clouds.assessment.model.entity.RatingRecordHistory;
import com.tree.clouds.assessment.service.RatingRecordHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;

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
     *  @param reportId
     * @param expertScore
     * @param illustrate
     * @param
     */
    @Override
    public void addRecord(String reportId, Double expertScore, String illustrate) {
        RatingRecordHistory ratingRecordHistory = new RatingRecordHistory();
        ratingRecordHistory.setReportId(reportId);
        ratingRecordHistory.setExpertScore(expertScore);
        ratingRecordHistory.setIllustrate(illustrate);
        this.save(ratingRecordHistory);
    }

    @Override
    public List<RatingRecordHistory> getHistoryList(String reportId) {
        return this.list(new QueryWrapper<RatingRecordHistory>().eq(RatingRecordHistory.REPORT_ID, reportId).orderByDesc(RatingRecordHistory.CREATED_TIME));

    }
}
