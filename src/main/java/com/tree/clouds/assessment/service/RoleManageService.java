package com.tree.clouds.assessment.service;

import com.tree.clouds.assessment.model.entity.RoleManage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.assessment.model.vo.DistributeRoleVO;

import java.util.List;

/**
 * <p>
 * 角色管理表 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
public interface RoleManageService extends IService<RoleManage> {

   List<RoleManage> getRoleByUserId(String userId);

    void distributeRole(DistributeRoleVO distributeRoleVO);
}
