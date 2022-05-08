package com.tree.clouds.assessment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.AuditLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.assessment.model.vo.*;

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


}
