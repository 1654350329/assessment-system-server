package com.tree.clouds.assessment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.tree.clouds.assessment.mapper.UnitUserMapper;
import com.tree.clouds.assessment.model.entity.UnitUser;
import com.tree.clouds.assessment.service.UnitUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 分组与角色管理中间表 服务实现类
 * </p>
 *
 * @author lzk
 * @since 2021-12-28
 */
@Service
public class UnitUserServiceImpl extends ServiceImpl<UnitUserMapper, UnitUser> implements UnitUserService {


    @Override
    public void saveUnitUser(String userId, String unitId) {
        UnitUser unitUser = new UnitUser();
        unitUser.setUserId(userId);
        unitUser.setUnitId(unitId);
        this.save(unitUser);
    }

    @Override
    public void removeUserByUserId(String userId) {
        QueryWrapper<UnitUser> wrapper = new QueryWrapper<>();
        wrapper.eq(UnitUser.USER_ID, userId);
        this.remove(wrapper);
    }

}
