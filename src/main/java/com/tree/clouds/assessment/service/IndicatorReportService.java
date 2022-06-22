package com.tree.clouds.assessment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.IndicatorReport;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.assessment.model.vo.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据上报 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
public interface IndicatorReportService extends IService<IndicatorReport> {

    IPage<IndicatorReportVO> assessmentList(AssessmentPageVO assessmentPageVO, Integer type);

    IPage<AssessmentListVO> assessmentList(PageParam pageParam);

    void updateReport(UpdateReportVO updateReportVO, int progress);

    IndicatorReport getByReportIdAndUnitId(String detailId, String unitId);

    IPage<AssessmentErrorVO> assessmentErrorList(AssessmentPageVO assessmentPageVO);

    void updateProgress(String reportId, int progress);

    double getUserScore(String unitId, String assessmentYear);

    List<IndicatorsTreeTreeVO> getTreeById(String id, String unitId, String reportId, Integer indicatorsStatus, String content, int type);


    int getMaterial(Integer type, String unitId);

    int getReportSum(Integer type, String unitId);

    int getMaterial(String unitId, String expertUnit, Integer type);

    int getReportSum(String unitId, String expertUnit, Integer type);

    Integer getReviewedNumber(String unitId, int type, String year);

    Map<String, Object> getData(GetDataVO getDataVO);

    Double getUserScoreByUnit(String unitId, int year);

    Integer getAuditNumber(String unitId, int year);

    List<IndicatorsTreeTreeVO> scoreLeftTree(Integer year, String unitId, int progress);

}
