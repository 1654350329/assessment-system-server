package com.tree.clouds.assessment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.assessment.model.entity.AssessmentIndicators;
import com.tree.clouds.assessment.model.entity.SubmitLog;
import com.tree.clouds.assessment.model.vo.SubmitLogPageVO;
import com.tree.clouds.assessment.model.vo.SubmitLogVO;

/**
 * <p>
 * 报送日志 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-05-07
 */
public interface SubmitLogService extends IService<SubmitLog> {

    IPage<SubmitLogVO> submitLogPage(SubmitLogPageVO submitLogPageVO);

    void addLog(AssessmentIndicators indicators, Integer indicatorsStatus, String remark);
}
