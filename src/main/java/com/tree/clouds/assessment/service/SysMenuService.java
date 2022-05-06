package com.tree.clouds.assessment.service;

import com.tree.clouds.assessment.model.bo.SysMenuDto;
import com.tree.clouds.assessment.model.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.assessment.model.vo.SysMenuTreeVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
public interface SysMenuService extends IService<SysMenu> {
    List<SysMenuDto> getCurrentUserNav();

    List<SysMenuTreeVO> tree();

    List<String> getRole(String id);
}
