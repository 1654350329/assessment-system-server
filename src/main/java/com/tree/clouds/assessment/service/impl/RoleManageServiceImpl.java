package com.tree.clouds.assessment.service.impl;

import com.tree.clouds.assessment.model.entity.RoleManage;
import com.tree.clouds.assessment.mapper.RoleManageMapper;
import com.tree.clouds.assessment.service.RoleManageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色管理表 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@Service
public class RoleManageServiceImpl extends ServiceImpl<RoleManageMapper, RoleManage> implements RoleManageService {

    @Override
    public List<RoleManage> getRoleByUserId(String userId) {
        return this.baseMapper.getRoleByUserId(userId);
    }
}
