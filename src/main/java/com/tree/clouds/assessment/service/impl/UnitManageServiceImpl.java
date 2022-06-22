package com.tree.clouds.assessment.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.assessment.mapper.UnitManageMapper;
import com.tree.clouds.assessment.mapper.UnitUserMapper;
import com.tree.clouds.assessment.model.entity.RoleManage;
import com.tree.clouds.assessment.model.entity.UnitManage;
import com.tree.clouds.assessment.model.entity.UnitUser;
import com.tree.clouds.assessment.model.vo.UnitManagePageVO;
import com.tree.clouds.assessment.service.RoleManageService;
import com.tree.clouds.assessment.service.UnitManageService;
import com.tree.clouds.assessment.utils.BaseBusinessException;
import com.tree.clouds.assessment.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 分组管理 服务实现类
 * </p>
 *
 * @author lzk
 * @since 2021-12-28
 */
@Service
public class UnitManageServiceImpl extends ServiceImpl<UnitManageMapper, UnitManage> implements UnitManageService {

    @Autowired
    private UnitUserMapper unitUserMapper;
    @Autowired
    private RoleManageService roleManageService;

    @Override
    public IPage<UnitManage> groupManagePage(UnitManagePageVO unitManagePageVO) {
        List<RoleManage> roleManageList = roleManageService.getRoleByUserId(LoginUserUtil.getUserId());
        List<String> collect = roleManageList.stream().map(RoleManage::getRoleCode).collect(Collectors.toList());
        if (!collect.contains("ROLE_admin")) {
            unitManagePageVO.setUnitName(LoginUserUtil.getUnitName());
        }
        IPage<UnitManage> page = unitManagePageVO.getPage();
        return this.baseMapper.unitManagePage(page, unitManagePageVO);
    }

    @Override
    public void deleteGroupRole(List<String> ids) {
        for (String id : ids) {
            if (NumberUtil.isNumber(id) && Integer.parseInt(id) <= 10) {
                throw new BaseBusinessException(400, "基础单位不许删除!");
            }
        }
        List<UnitUser> unitUsers = unitUserMapper.selectList(new QueryWrapper<UnitUser>().in(UnitUser.Unit_ID, ids));
        if (CollUtil.isNotEmpty(unitUsers)) {
            throw new BaseBusinessException(400, "单位存在用户,不许删除!");
        }
        this.removeByIds(ids);
    }

    @Override
    public List<UnitManage> getListByType(Integer type, String unitName) {
        QueryWrapper<UnitManage> queryWrapper = new QueryWrapper<UnitManage>()
                .eq(UnitManage.UNIT_TYPE, type);
        if (StrUtil.isNotBlank(unitName)) {
            queryWrapper.like(UnitManage.UNIT_NAME, unitName);
        }
        return this.list(queryWrapper);
    }
}
