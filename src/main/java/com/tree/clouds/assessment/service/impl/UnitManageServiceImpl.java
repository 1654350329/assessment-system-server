package com.tree.clouds.assessment.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.assessment.mapper.UnitManageMapper;
import com.tree.clouds.assessment.mapper.UnitUserMapper;
import com.tree.clouds.assessment.model.entity.UnitManage;
import com.tree.clouds.assessment.model.entity.UnitUser;
import com.tree.clouds.assessment.model.vo.UnitManagePageVO;
import com.tree.clouds.assessment.service.UnitManageService;
import com.tree.clouds.assessment.utils.BaseBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public IPage<UnitManage> groupManagePage(UnitManagePageVO unitManagePageVO) {
        IPage<UnitManage> page = unitManagePageVO.getPage();
        return this.baseMapper.unitManagePage(page, unitManagePageVO);
    }

    @Override
    public void deleteGroupRole(List<String> ids) {
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
