package com.tree.clouds.assessment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.bo.UserManageBO;
import com.tree.clouds.assessment.model.entity.UserManage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.assessment.model.vo.UpdatePasswordVO;
import com.tree.clouds.assessment.model.vo.UserManagePageVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 用户管理 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
public interface UserManageService extends IService<UserManage> {

    void rebuildPassword(List<String> ids);

    void userStatus(List<String> ids, int status);

    IPage<UserManageBO> userManagePage(UserManagePageVO userManagePageVO);

    UserManage getUserByAccount(String account);

    String getUserAuthorityInfo(String userId);

    void addUserManage(UserManageBO userManageBO);

    void updateUserManage(UserManageBO userManageBO);

    void deleteUserManage(String userId);

    void clearUserAuthorityInfo(String userId);

    void clearUserAuthorityInfoByMenuId(String menuId);

    void clearUserAuthorityInfoByRoleId(String roleId);

    void updatePassword(UpdatePasswordVO updatePasswordVO);
}
