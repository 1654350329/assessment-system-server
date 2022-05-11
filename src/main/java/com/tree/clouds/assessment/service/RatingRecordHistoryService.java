package com.tree.clouds.assessment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.assessment.model.entity.RatingRecordHistory;

import java.util.List;

/**
 * <p>
 * 评分历史记录 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-05-08
 */
public interface RatingRecordHistoryService extends IService<RatingRecordHistory> {

    void addRecord(String detailId, Double expertScore, String illustrate);
    List<RatingRecordHistory> getHistoryList(String reportId);
}
