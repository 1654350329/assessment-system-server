package com.tree.clouds.assessment.service;

import com.tree.clouds.assessment.model.entity.RoleManage;
import com.tree.clouds.assessment.model.entity.RoleUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色与用户管理中间表 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
public interface RoleUserService extends IService<RoleUser> {
    void addRole(List<String> roleIds, String userId);

    List<RoleManage> getRoleByUserId(String userId);

    boolean removeRole(String userId);
}
