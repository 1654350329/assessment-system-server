package com.tree.clouds.assessment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.IndicatorReport;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.assessment.model.vo.*;

import java.util.List;

/**
 * <p>
 * 数据上报 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
public interface IndicatorReportService extends IService<IndicatorReport> {

    IPage<IndicatorReportVO> assessmentList(AssessmentPageVO assessmentPageVO,Integer type);

    void updateReport(UpdateReportVO updateReportVO,int progress);

    IndicatorReport getByDetailIdAndUnitId(String detailId, String unitId);

    IPage<AssessmentErrorVO> assessmentErrorList(AssessmentPageVO assessmentPageVO);

    void updateProgress(String reportId, int progress);

}
