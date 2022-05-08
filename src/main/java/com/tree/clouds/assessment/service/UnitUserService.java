package com.tree.clouds.assessment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.assessment.model.entity.UnitUser;

/**
 * <p>
 * 分组与角色管理中间表 服务类
 * </p>
 *
 * @author lzk
 * @since 2021-12-28
 */
public interface UnitUserService extends IService<UnitUser> {

    void saveUnitUser(String userId, String groupId);

    void removeUserByUserId(String userId);
}
