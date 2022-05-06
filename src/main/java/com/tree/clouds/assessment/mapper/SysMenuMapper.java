package com.tree.clouds.assessment.mapper;

import com.tree.clouds.assessment.model.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    List<String> getMenuIdByRoleId(@Param("roleId")String roleId);
}
