package com.tree.clouds.assessment.mapper;

import com.tree.clouds.assessment.model.entity.RoleManage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 角色管理表 Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
public interface RoleManageMapper extends BaseMapper<RoleManage> {

    List<RoleManage> getRoleByUserId(String userId);
}
