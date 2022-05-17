package com.tree.clouds.assessment.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tree.clouds.assessment.model.entity.*;
import com.tree.clouds.assessment.mapper.AuditLogMapper;
import com.tree.clouds.assessment.model.vo.*;
import com.tree.clouds.assessment.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.assessment.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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
    @Autowired
    private ScoreRecordService scoreRecordService;
    @Autowired
    private AssessmentIndicatorsDetailService detailService;
    @Autowired
    private ComprehensiveAssessmentService comprehensiveAssessmentService;
    @Autowired
    private UnitAssessmentService unitAssessmentService;

    @Override
    @Transactional
    public void updateAudit(UpdateAuditVO updateAuditVO) {
        //修改审核状态
        IndicatorReport indicatorReport = indicatorReportService.getById(updateAuditVO.getId());
        indicatorReport.setReportStatus(updateAuditVO.getIndicatorsStatus());

        indicatorReport.setReportProgress(1);
        //修改截止日期
        AssessmentIndicators indicators = assessmentIndicatorsService.getById(indicatorReport.getIndicatorsId());
        if (indicators.getIndicatorsName().equals("机制创新")&& updateAuditVO.getIndicatorsStatus() == 1){
            indicatorReport.setReportProgress(2);
        }
        if (updateAuditVO.getIndicatorsStatus() == 0){
            indicatorReport.setReportProgress(0);
        }
        AssessmentIndicatorsDetail detail = detailService.getById(indicatorReport.getDetailId());
        if (StrUtil.isNotBlank(updateAuditVO.getExpirationDate())) {
            assessmentIndicatorsService.updateExpirationDate(indicators.getAssessmentYear(), updateAuditVO.getExpirationDate());
        }
        //新增报送历史日志
        submitLogService.addLog(indicators, detail.getAssessmentCriteria(), updateAuditVO.getIndicatorsStatus(), updateAuditVO.getRemark(), LoginUserUtil.getUnitId(), indicatorReport.getReportTime(), indicatorReport.getReportId());
        //修改上报进度
        indicatorReportService.updateById(indicatorReport);
        //修改审核记录
        AuditLog auditLog = this.getByReportId(indicatorReport.getReportId());
        if (auditLog == null) {
            auditLog = new AuditLog();
        }
        auditLog.setDetailId(detail.getDetailId());
        auditLog.setReportId(indicatorReport.getReportId());
        auditLog.setRemark(updateAuditVO.getRemark());
        auditLog.setExpirationDate(updateAuditVO.getExpirationDate());
        auditLog.setIndicatorsStatus(updateAuditVO.getIndicatorsStatus());
        this.saveOrUpdate(auditLog, new QueryWrapper<AuditLog>().eq(AuditLog.REPORT_ID, indicatorReport.getReportId()));
        if ( updateAuditVO.getIndicatorsStatus() == 1) {
            if (!indicators.getIndicatorsName().equals("机制创新")){
                ScoreRecord scoreRecord = new ScoreRecord();
                scoreRecord.setReportId(updateAuditVO.getId());
                scoreRecord.setScoreType(0);
                scoreRecord.setExpertStatus(0);
                scoreRecordService.saveOrUpdate(scoreRecord,new QueryWrapper<ScoreRecord>().eq(ScoreRecord.REPORT_ID,updateAuditVO.getId()));
            }

        }

    }


    @Override
    public AuditLog getAudit(String id) {
        return this.getOne(new QueryWrapper<AuditLog>().eq(AuditLog.DETAIL_ID, id));
    }

    @Override
    public AuditLog getByReportId(String id) {
        return this.getOne(new QueryWrapper<AuditLog>().eq(AuditLog.REPORT_ID, id));
    }

}
