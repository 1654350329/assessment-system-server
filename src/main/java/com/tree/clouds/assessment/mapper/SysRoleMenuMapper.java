package com.tree.clouds.assessment.mapper;

import com.tree.clouds.assessment.model.entity.SysRoleMenu;
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
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {
    List<String> getNavMenuIds(@Param("userId") String userId);
}
