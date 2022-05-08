package com.tree.clouds.assessment.service.impl;

import com.tree.clouds.assessment.model.entity.AssessmentIndicators;
import com.tree.clouds.assessment.model.entity.AuditLog;
import com.tree.clouds.assessment.mapper.AuditLogMapper;
import com.tree.clouds.assessment.model.entity.IndicatorReport;
import com.tree.clouds.assessment.model.vo.*;
import com.tree.clouds.assessment.service.AssessmentIndicatorsService;
import com.tree.clouds.assessment.service.AuditLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.assessment.service.IndicatorReportService;
import com.tree.clouds.assessment.service.SubmitLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 审核日志 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@Service
public class AuditLogServiceImpl extends ServiceImpl<AuditLogMapper, AuditLog> implements AuditLogService {

    @Autowired
    private IndicatorReportService indicatorReportService;
    @Autowired
    private AssessmentIndicatorsService assessmentIndicatorsService;
    @Autowired
    private SubmitLogService submitLogService;

    @Override
    public void updateAudit(UpdateAuditVO updateAuditVO) {
        //修改审核状态
        IndicatorReport indicatorReport=indicatorReportService.getByDetailIdAndUnitId(updateAuditVO.getIndicatorsId(),updateAuditVO.getUnitId());
        indicatorReport.setReportStatus(updateAuditVO.getIndicatorsStatus());
        indicatorReport.setReportProgress(1);
        //修改截止日期
        AssessmentIndicators indicators = assessmentIndicatorsService.getById(updateAuditVO.getIndicatorsId());
        assessmentIndicatorsService.updateExpirationDate(indicators.getAssessmentYear(),updateAuditVO.getExpirationDate());
        //新增报送历史日志
        submitLogService.addLog(indicators,updateAuditVO.getIndicatorsStatus(),updateAuditVO.getRemark());
    }

}
