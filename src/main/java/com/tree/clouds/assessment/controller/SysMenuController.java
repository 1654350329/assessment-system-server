package com.tree.clouds.assessment.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tree.clouds.assessment.common.RestResponse;
import com.tree.clouds.assessment.model.bo.SysMenuDto;
import com.tree.clouds.assessment.model.entity.SysMenu;
import com.tree.clouds.assessment.model.entity.SysRoleMenu;
import com.tree.clouds.assessment.model.entity.UserManage;
import com.tree.clouds.assessment.model.vo.PublicIdReqVO;
import com.tree.clouds.assessment.model.vo.SysMenuTreeVO;
import com.tree.clouds.assessment.model.vo.SysMenuVO;
import com.tree.clouds.assessment.model.vo.SysMenuVOS;
import com.tree.clouds.assessment.service.SysMenuService;
import com.tree.clouds.assessment.service.SysRoleMenuService;
import com.tree.clouds.assessment.service.UserManageService;
import com.tree.clouds.assessment.utils.LoginUserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@RestController
@RequestMapping("/sys-menu")
@Api(tags = "菜单管理")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Autowired
    private UserManageService userManageService;

    @GetMapping("/nav")
    @ApiOperation(value = "当前用户的菜单和权限信息")
    public RestResponse<Map<Object, Object>> nav() {

        UserManage userByAccount = userManageService.getUserByAccount(LoginUserUtil.getUserAccount());

        // 获取权限信息
        String authorityInfo = userManageService.getUserAuthorityInfo(userByAccount.getUserId());// ROLE_admin,ROLE_normal,sys:user:list,....
        String[] authorityInfoArray = StringUtils.tokenizeToStringArray(authorityInfo, ",");

        // 获取导航栏信息
        List<SysMenuDto> navs = sysMenuService.getCurrentUserNav();

        return RestResponse.ok(MapUtil.builder()
                .put("authoritys", authorityInfoArray)
                .put("nav", navs)
                .map()
        );
    }

    @GetMapping("/info")
//    @PreAuthorize("hasAuthority('sys:menu:list')")
    @ApiOperation(value = "菜单信息")
    public RestResponse<SysMenu> info(@RequestBody PublicIdReqVO publicIdReqVO) {
        return RestResponse.ok(sysMenuService.getById(publicIdReqVO.getId()));
    }

    @PostMapping("/getRole")
    @ApiOperation(value = "获取角色菜单")
    public RestResponse<List<String>> getRole(@RequestBody PublicIdReqVO publicIdReqVO) {
        List<String> stringList = sysMenuService.getRole(publicIdReqVO.getId());
        return RestResponse.ok(stringList);
    }

    @GetMapping("/list")
//    @PreAuthorize("hasAuthority('sys:menu:list')")
    @ApiOperation(value = "菜单树")
    public RestResponse<List<SysMenuTreeVO>> list() {
        List<SysMenuTreeVO> menus = sysMenuService.tree();
        return RestResponse.ok(menus);
    }

    @PostMapping("/save")
//    @PreAuthorize("hasAuthority('sys:menu:save')")
    @ApiOperation(value = "菜单保存")
    public RestResponse<Boolean> save(@Validated @RequestBody SysMenuVOS sysMenuVOS) {
        for (SysMenuVO sysMenuVO : sysMenuVOS.getSysMenuVOS()) {
            SysMenu sysMenu = BeanUtil.toBean(sysMenuVO, SysMenu.class);
            sysMenuService.save(sysMenu);
        }
        return RestResponse.ok();
    }

    @PostMapping("/update")
//    @PreAuthorize("hasAuthority('sys:menu:update')")
    @ApiOperation(value = "菜单更新")
    public RestResponse<Boolean> update(@Validated @RequestBody SysMenuVOS sysMenuVOS) {
        for (SysMenuVO sysMenuVO : sysMenuVOS.getSysMenuVOS()) {
            SysMenu sysMenu = BeanUtil.toBean(sysMenuVO, SysMenu.class);
            sysMenuService.updateById(sysMenu);
            if (CollUtil.isNotEmpty(sysMenuVO.getChildren())) {
                for (SysMenuVO child : sysMenuVO.getChildren()) {
                    SysMenu sysMenu1 = BeanUtil.toBean(child, SysMenu.class);
                    sysMenuService.updateById(sysMenu1);
                    if (CollUtil.isNotEmpty(child.getChildren())) {
                        for (SysMenuVO childChild : child.getChildren()) {
                            SysMenu sysMenu2 = BeanUtil.toBean(childChild, SysMenu.class);
                            sysMenuService.updateById(sysMenu2);
                        }

                    }
                }

            }
            userManageService.clearUserAuthorityInfoByMenuId(sysMenu.getId());
        }
        // 清除所有与该菜单相关的权限缓存
        return RestResponse.ok();
    }

    @PostMapping("/delete")
//    @PreAuthorize("hasAuthority('sys:menu:delete')")
    @ApiOperation(value = "菜单删除")
    public RestResponse<Boolean> delete(@RequestBody PublicIdReqVO publicIdReqVO) {

        int count = sysMenuService.count(new QueryWrapper<SysMenu>().eq("parent_id", publicIdReqVO.getId()));
        if (count > 0) {
            return RestResponse.fail(400, "请先删除子菜单");
        }

        // 清除所有与该菜单相关的权限缓存
        userManageService.clearUserAuthorityInfoByMenuId(publicIdReqVO.getId());

        sysMenuService.removeById(publicIdReqVO.getId());

        // 同步删除中间关联表
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("menu_id", publicIdReqVO.getId()));
        return RestResponse.ok(true);
    }
}

