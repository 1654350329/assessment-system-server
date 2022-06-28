package com.tree.clouds.assessment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tree.clouds.assessment.mapper.SysRoleMenuMapper;
import com.tree.clouds.assessment.model.bo.SysMenuDto;
import com.tree.clouds.assessment.model.entity.RoleManage;
import com.tree.clouds.assessment.model.entity.SysMenu;
import com.tree.clouds.assessment.mapper.SysMenuMapper;
import com.tree.clouds.assessment.model.entity.UnitManage;
import com.tree.clouds.assessment.model.entity.UserManage;
import com.tree.clouds.assessment.model.vo.SysMenuTreeVO;
import com.tree.clouds.assessment.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.assessment.utils.BaseBusinessException;
import com.tree.clouds.assessment.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    @Autowired
    private UserManageService sysUserService;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Autowired
    private RoleManageService roleManageService;
    @Autowired
    private AssessmentIndicatorsService assessmentIndicatorsService;
    @Autowired
    private UnitManageService unitManageService;

    @Override
    public List<SysMenuDto> getCurrentUserNav() {
        UserManage sysUser = sysUserService.getUserByAccount(LoginUserUtil.getUserAccount());
        List<String> menuIds = sysRoleMenuMapper.getNavMenuIds(sysUser.getUserId());
        List<SysMenu> menus = this.listByIds(menuIds);
        //为牵头单位且为管理员 有评分功能
        List<RoleManage> roleManages = roleManageService.getRoleByUserId(LoginUserUtil.getUserId());
        List<String> roleCodes = roleManages.stream().map(RoleManage::getRoleCode).collect(Collectors.toList());
        if (assessmentIndicatorsService.isExpertUnit(LoginUserUtil.getUnitId()) && roleCodes.contains("ROLE_user_admin")) {
            List<SysMenu> list = this.list(new QueryWrapper<SysMenu>().eq(SysMenu.NAME, "专家评分")
                    .or()
                    .eq(SysMenu.NAME, "评分列表")
                    .or()
                    .eq(SysMenu.NAME, "复评列表")
            );
            menus.addAll(list);
        }

        UnitManage unitManage = unitManageService.getById(LoginUserUtil.getUnitId());
        if (unitManage != null) {
            if (roleCodes.contains("ROLE_user_admin") && unitManage.getUnitType() == 0) {
                menus = menus.stream().filter(sysMenu -> !sysMenu.getName().equals("区县单位加减分一览表"))
                        .filter(sysMenu -> !sysMenu.getName().equals("区县单位综合评定"))
                        .collect(Collectors.toList());
            }
            //区县单位不展示初审管理下属单位管理
            if (roleCodes.contains("ROLE_user_admin") && unitManage.getUnitType() == 1) {
                menus = menus.stream().filter(sysMenu -> !sysMenu.getName().equals("下属单位加减分一览表"))
                        .filter(sysMenu -> !sysMenu.getName().equals("初审管理"))
                        .filter(sysMenu -> !sysMenu.getName().equals("考核与材料报送"))
                        .filter(sysMenu -> !sysMenu.getName().equals("下属单位考核指标"))
                        .filter(sysMenu -> !sysMenu.getName().equals("考核指标"))
                        .filter(sysMenu -> !sysMenu.getName().equals("驳回列表"))
                        .filter(sysMenu -> !sysMenu.getName().equals("下属单位综合评定"))
                        .collect(Collectors.toList());
            }
        }

        ArrayList<SysMenuTreeVO> sysMenuVOS = new ArrayList<>();
        for (SysMenu sysMenu : menus) {
            SysMenuTreeVO sysMenuVO = BeanUtil.toBean(sysMenu, SysMenuTreeVO.class);
            sysMenuVOS.add(sysMenuVO);
        }
        // 转树状结构
        List<SysMenuTreeVO> menuTree = buildTreeMenu(sysMenuVOS);

        // 实体转DTO
        return convert(menuTree);
    }

    @Override
    public List<SysMenuTreeVO> tree() {
        // 获取所有菜单信息
        List<SysMenu> sysMenus = this.list();
        ArrayList<SysMenuTreeVO> sysMenuVOS = new ArrayList<>();
        for (SysMenu sysMenu : sysMenus) {
            SysMenuTreeVO sysMenuVO = BeanUtil.toBean(sysMenu, SysMenuTreeVO.class);
            sysMenuVOS.add(sysMenuVO);
        }
        // 转成树状结构
        return buildTreeMenu(sysMenuVOS);
    }

    @Override
    public List<String> getRole(String id) {
        return this.baseMapper.getMenuIdByRoleId(id);
    }

    private List<SysMenuDto> convert(List<SysMenuTreeVO> menuTree) {
        List<SysMenuDto> menuDtos = new ArrayList<>();

        menuTree.forEach(m -> {
            SysMenuDto dto = new SysMenuDto();

            dto.setId(m.getId());
            dto.setTitle(m.getName());

            if (m.getChildren().size() > 0) {

                // 子节点调用当前方法进行再次转换
                dto.setChildren(convert(m.getChildren()));
            }

            menuDtos.add(dto);
        });

        return menuDtos;
    }

    private List<SysMenuTreeVO> buildTreeMenu(List<SysMenuTreeVO> menus) {

        List<SysMenuTreeVO> finalMenus = new ArrayList<>();

        // 先各自寻找到各自的孩子
        for (SysMenuTreeVO menu : menus) {

            for (SysMenuTreeVO e : menus) {
                if (menu.getId().equals(e.getParentId())) {
                    menu.getChildren().add(e);
                }
            }

            // 提取出父节点
            if (menu.getParentId().equals("0")) {
                finalMenus.add(menu);
            }
        }

        return finalMenus;
    }
}
