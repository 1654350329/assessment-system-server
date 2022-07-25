package com.tree.clouds.assessment.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.bo.UserManageBO;
import com.tree.clouds.assessment.model.entity.RoleManage;
import com.tree.clouds.assessment.model.entity.UserManage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tree.clouds.assessment.model.vo.UserManagePageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户管理 Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
public interface UserManageMapper extends BaseMapper<UserManage> {
    IPage<UserManageBO> userManagePage(IPage<UserManageBO> page, @Param("userManagePageVO") UserManagePageVO userManagePageVO);

    List<UserManage> listByMenuId(@Param("menuId") String menuId);

    UserManage isExist(String account, String phone);

    List<UserManage> listByRoleId(@Param("roleId") String roleId);

    List<RoleManage> getRoleById(String userId);

    UserManage getInfo(String userId);

    List<UserManage> getListByIds(List<String> ids);
}
