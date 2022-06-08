package com.tree.clouds.assessment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.assessment.model.entity.AuditLog;
import com.tree.clouds.assessment.model.vo.UpdateAuditVO;

/**
 * <p>
 * 审核日志 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
public interface AuditLogService extends IService<AuditLog> {

    void updateAudit(UpdateAuditVO updateAuditVO);

    AuditLog getAudit(String id);

    AuditLog getByReportId(String id);
}
