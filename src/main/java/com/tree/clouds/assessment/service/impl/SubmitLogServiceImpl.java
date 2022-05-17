package com.tree.clouds.assessment.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.assessment.mapper.SubmitLogMapper;
import com.tree.clouds.assessment.model.entity.AssessmentIndicators;
import com.tree.clouds.assessment.model.entity.SubmitLog;
import com.tree.clouds.assessment.model.vo.SubmitLogPageVO;
import com.tree.clouds.assessment.model.vo.SubmitLogVO;
import com.tree.clouds.assessment.service.SubmitLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 报送日志 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-05-07
 */
@Service
public class SubmitLogServiceImpl extends ServiceImpl<SubmitLogMapper, SubmitLog> implements SubmitLogService {

    @Override
    public IPage<SubmitLogVO> submitLogPage(SubmitLogPageVO submitLogPageVO) {
        IPage<SubmitLogVO> page = submitLogPageVO.getPage();
        page=this.baseMapper.submitLogPage(page,submitLogPageVO);
        return page;
    }

    @Override
    public void addLog(AssessmentIndicators indicators, String assessmentCriteria, Integer indicatorsStatus, String remark, String unitId, String time,String reportId) {
        SubmitLog submitLog =new SubmitLog();
        submitLog.setAssessmentYear(indicators.getAssessmentYear());
        submitLog.setAssessmentCriteria(assessmentCriteria);
        submitLog.setIndicatorsName(indicators.getIndicatorsName());
        submitLog.setIndicatorsStatus(indicatorsStatus);
        submitLog.setEvaluationMethod(time);
        submitLog.setExpirationDate(indicators.getExpirationDate());
        submitLog.setRemark(remark);
        submitLog.setUnitId(unitId);
        submitLog.setReportId(reportId);
        this.save(submitLog);
    }
}
