package com.tree.clouds.assessment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tree.clouds.assessment.mapper.RoleUserMapper;
import com.tree.clouds.assessment.mapper.SysRoleMenuMapper;
import com.tree.clouds.assessment.model.entity.RoleManage;
import com.tree.clouds.assessment.mapper.RoleManageMapper;
import com.tree.clouds.assessment.model.entity.SysMenu;
import com.tree.clouds.assessment.model.entity.SysRoleMenu;
import com.tree.clouds.assessment.model.vo.DistributeRoleVO;
import com.tree.clouds.assessment.service.RoleManageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.assessment.service.SysMenuService;
import com.tree.clouds.assessment.service.UserManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Autowired
    private UserManageService userManageService;
    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public List<RoleManage> getRoleByUserId(String userId) {
        return this.baseMapper.getRoleByUserId(userId);
    }

    @Override
    public void distributeRole(DistributeRoleVO distributeRoleVO) {
        this.sysRoleMenuMapper.delete(new QueryWrapper<SysRoleMenu>().eq(SysRoleMenu.ROLE_ID, distributeRoleVO.getRoleId()));
        List<String> menuIds = distributeRoleVO.getMenuIds();
        Set<String> menuSet = new HashSet<>(menuIds);

        for (String menuId : menuIds) {
            SysMenu sysMenu = this.sysMenuService.getById(menuId);
            String pid = sysMenu.getParentId();
            while (!pid.equals("0")) {
                menuSet.add(pid);
                pid = this.sysMenuService.getById(pid).getParentId();
            }
        }
        for (String s : menuSet) {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(s);
            sysRoleMenu.setRoleId(distributeRoleVO.getRoleId());
            this.sysRoleMenuMapper.insert(sysRoleMenu);
        }
        userManageService.clearUserAuthorityInfoByRoleId(distributeRoleVO.getRoleId());
    }
}
