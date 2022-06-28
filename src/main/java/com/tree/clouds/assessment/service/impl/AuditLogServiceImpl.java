package com.tree.clouds.assessment.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.assessment.mapper.AuditLogMapper;
import com.tree.clouds.assessment.model.entity.*;
import com.tree.clouds.assessment.model.vo.UpdateAuditVO;
import com.tree.clouds.assessment.service.*;
import com.tree.clouds.assessment.utils.BaseBusinessException;
import com.tree.clouds.assessment.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private RoleManageService roleManageService;
    @Autowired
    private MatterListService matterListService;

    @Override
    @Transactional
    public void updateAudit(UpdateAuditVO updateAuditVO) {
        List<RoleManage> roleManages = roleManageService.getRoleByUserId(LoginUserUtil.getUserId());
        List<String> roleCodes = roleManages.stream().map(RoleManage::getRoleCode).collect(Collectors.toList());
        if (!(roleCodes.contains("ROLE_admin") || roleCodes.contains("ROLE_user_admin"))) {
            throw new BaseBusinessException(400, "没有审核权限!");
        }

        //修改审核状态
        IndicatorReport indicatorReport = indicatorReportService.getById(updateAuditVO.getId());
        indicatorReport.setReportStatus(updateAuditVO.getIndicatorsStatus());
        if (StrUtil.isNotBlank(updateAuditVO.getExpirationDate()) && !updateAuditVO.getExpirationDate().contains(":")) {
            updateAuditVO.setExpirationDate(updateAuditVO.getExpirationDate() + " 00:00:00");
        }
        if (StrUtil.isNotBlank(updateAuditVO.getExpirationDate()) && new Date().getTime() > DateUtil.parseDateTime(updateAuditVO.getExpirationDate()).getTime()) {
            throw new BaseBusinessException(400, "截止日期必须大于当前时间!");
        }
        if (StrUtil.isNotBlank(updateAuditVO.getExpirationDate())) {
            indicatorReport.setExpirationDate(updateAuditVO.getExpirationDate());
        }

        AssessmentIndicators indicators = assessmentIndicatorsService.getById(indicatorReport.getIndicatorsId());
        AssessmentIndicatorsDetail detail = detailService.getById(indicatorReport.getDetailId());
        AssessmentIndicators id = this.assessmentIndicatorsService.getById(detail.getProjectId());
        if (updateAuditVO.getIndicatorsStatus() == 1) {
            if (id.getEvaluationMethod() == 2) {
                indicatorReport.setReportProgress(3);
            }
            if (id.getEvaluationMethod() == 1) {
                indicatorReport.setReportProgress(2);
            }
        }

        if (updateAuditVO.getIndicatorsStatus() == 0) {
            indicatorReport.setReportProgress(0);
            //添加到驳回待办
            matterListService.addMatter(indicators.getIndicatorsName() + "-" + detail.getAssessmentCriteria() + "材料驳回", indicatorReport.getUnitId(), indicatorReport.getReportId(), null, 2, indicators.getAssessmentYear(), indicatorReport.getIndicatorsId());
        }

        //修改截止日期
        //新增报送历史日志
        submitLogService.addLog(indicators, detail.getAssessmentCriteria(), updateAuditVO.getIndicatorsStatus(), updateAuditVO.getRemark(),
                LoginUserUtil.getUnitId(), indicatorReport.getReportTime(), indicatorReport.getReportId(), updateAuditVO.getExpirationDate());
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
        if (updateAuditVO.getIndicatorsStatus() == 1) {

            if (id.getEvaluationMethod() == 1) {
                ScoreRecord scoreRecord = new ScoreRecord();
                scoreRecord.setReportId(updateAuditVO.getId());
                scoreRecord.setScoreType(0);
                scoreRecord.setExpertStatus(0);
                scoreRecordService.saveOrUpdate(scoreRecord, new QueryWrapper<ScoreRecord>().eq(ScoreRecord.REPORT_ID, updateAuditVO.getId()));
                //添加到专家待评
                AssessmentIndicators project = assessmentIndicatorsService.getById(detail.getTaskId());
                matterListService.addMatter(indicators.getIndicatorsName() + "-" + detail.getAssessmentCriteria() + "材料待审", indicatorReport.getUnitId(), indicatorReport.getReportId(), project.getUnitId(), 3, indicators.getAssessmentYear(), indicatorReport.getIndicatorsId());
            }

        }
        scoreRecordService.isCompleteResult(indicatorReport.getUnitId(), indicators.getAssessmentYear());
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
