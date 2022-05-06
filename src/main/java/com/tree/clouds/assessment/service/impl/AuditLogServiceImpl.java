package com.tree.clouds.assessment.service.impl;

import com.tree.clouds.assessment.model.entity.AuditLog;
import com.tree.clouds.assessment.mapper.AuditLogMapper;
import com.tree.clouds.assessment.service.AuditLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}
