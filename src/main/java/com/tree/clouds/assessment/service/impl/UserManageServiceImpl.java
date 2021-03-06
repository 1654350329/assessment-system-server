package com.tree.clouds.assessment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.common.Constants;
import com.tree.clouds.assessment.mapper.SysRoleMenuMapper;
import com.tree.clouds.assessment.model.bo.UserManageBO;
import com.tree.clouds.assessment.model.entity.RoleManage;
import com.tree.clouds.assessment.model.entity.SysMenu;
import com.tree.clouds.assessment.model.entity.UserManage;
import com.tree.clouds.assessment.mapper.UserManageMapper;
import com.tree.clouds.assessment.model.vo.UpdatePasswordVO;
import com.tree.clouds.assessment.model.vo.UserManagePageVO;
import com.tree.clouds.assessment.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.assessment.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户管理 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@Service
public class UserManageServiceImpl extends ServiceImpl<UserManageMapper, UserManage> implements UserManageService {
    @Autowired
    private RoleUserService roleUserService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UnitUserService unitUserService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private RoleManageService roleManageService;

    @Override
    @Transactional
    public void rebuildPassword(List<String> ids) {
        // 加密后密码
        String password = bCryptPasswordEncoder.encode("888888");
        List<RoleManage> roleManages = roleManageService.getRoleByUserId(LoginUserUtil.getUserId());
        List<String> codes = roleManages.stream().map(RoleManage::getRoleCode).collect(Collectors.toList());

        List<UserManage> userManages = this.baseMapper.getListByIds(ids);
        List<UserManage> manageList = userManages.stream().filter(userManage -> !userManage.getUnitId().equals(LoginUserUtil.getUnitId())).collect(Collectors.toList());
        if (!codes.contains("ROLE_admin") && CollUtil.isNotEmpty(manageList)) {
            throw new BaseBusinessException(401, "没有操作权限!");
        }

        if (!(codes.contains("ROLE_admin") || codes.contains("ROLE_up_user"))) {
            throw new BaseBusinessException(401, "没有操作权限");
        }
        if (!codes.contains("ROLE_admin") && codes.contains("ROLE_up_user")) {
            for (String id : ids) {
                List<RoleManage> roles = roleManageService.getRoleByUserId(id);
                List<RoleManage> role_admin = roles.stream().filter(roleManage -> roleManage.getRoleCode().equals("ROLE_admin")).limit(1).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(role_admin)) {
                    throw new BaseBusinessException(401, "没有操作权限!");
                }
            }
        }
        userManages.forEach(userManage -> {
            userManage.setPassword(password);
            redisUtil.hdel(Constants.ERROR_LOGIN, userManage.getAccount());
            redisUtil.hdel(Constants.LOCK_ACCOUNT, userManage.getAccount());
        });
        this.updateBatchById(userManages);
    }

    @Override
    public void userStatus(List<String> ids, int status) {
        List<UserManage> userManages = this.listByIds(ids);
        userManages.forEach(userManage -> userManage.setAccountStatus(status));
        this.updateBatchById(userManages);
    }


    @Override
    public IPage<UserManageBO> userManagePage(UserManagePageVO userManagePageVO) {
        List<RoleManage> roleManageList = roleManageService.getRoleByUserId(LoginUserUtil.getUserId());
        List<String> collect = roleManageList.stream().map(RoleManage::getRoleCode).collect(Collectors.toList());
        if (!collect.contains("ROLE_admin")) {
            userManagePageVO.setUnitId(LoginUserUtil.getUnitId());
        }
        IPage<UserManageBO> page = userManagePageVO.getPage();
        IPage<UserManageBO> userManageVOIPage = this.baseMapper.userManagePage(page, userManagePageVO);
        List<UserManageBO> records = userManageVOIPage.getRecords();
        for (UserManageBO record : records) {
            List<RoleManage> roleManages = roleUserService.getRoleByUserId(record.getUserId());
            record.setRoleIds(roleManages.stream().map(RoleManage::getRoleId).collect(Collectors.toList()));
            List<String> roleNames = roleManages.stream().map(RoleManage::getRoleName).collect(Collectors.toList());
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < roleNames.size(); i++) {
                if (i == roleNames.size() - 1) {
                    stringBuilder.append(roleNames.get(i));
                } else {
                    stringBuilder.append(roleNames.get(i)).append("-");
                }
            }
            record.setRoleName(stringBuilder.toString());
            record.setReviewAuthority(record.getRoleIds().contains("4") ? 1 : 0);
            record.setPassword(null);

        }
        return userManageVOIPage;
    }

    @Override
    public UserManage getUserByAccount(String account) {
        QueryWrapper<UserManage> wrapper = new QueryWrapper<>();
        wrapper.eq(UserManage.ACCOUNT, account);
        wrapper.eq(UserManage.DEL, 0);
        return this.getOne(wrapper);
    }

    @Override
    public String getUserAuthorityInfo(String userId) {

        //  ROLE_admin,ROLE_normal,sys:user:list,....
        String authority = "";
        if (redisUtil.hasKey("GrantedAuthority:" + userId)) {
            authority = (String) redisUtil.get("GrantedAuthority:" + userId);
        } else {
            //获取角色
            List<RoleManage> roles = this.roleUserService.getRoleByUserId(userId);
            authority = roles.stream().map(RoleManage::getRoleCode).collect(Collectors.joining(","));
            List<String> menuIds = sysRoleMenuMapper.getNavMenuIds(userId);
            List<SysMenu> menus = this.sysMenuService.listByIds(menuIds);
            String perms = menus.stream().map(SysMenu::getPerms).collect(Collectors.joining(","));
            authority = authority.concat(",").concat(perms);
            redisUtil.set("GrantedAuthority:" + userId, authority, 60 * 60);
        }
        return authority;
    }

    @Override
    public void clearUserAuthorityInfo(String userId) {
        redisUtil.del("GrantedAuthority:" + userId);
    }

    @Override
    @Transactional
    public void addUserManage(UserManageBO userManageBO) {
        List<RoleManage> roleManageList = roleManageService.getRoleByUserId(LoginUserUtil.getUserId());
        List<String> collect = roleManageList.stream().map(RoleManage::getRoleCode).collect(Collectors.toList());
        if (!collect.contains("ROLE_admin") && !userManageBO.getUnitId().equals(LoginUserUtil.getUnitId())) {
            throw new BaseBusinessException(400, "只能添加当前单位用户!");
        }
        if (StrUtil.isNotBlank(userManageBO.getUserId())) {
            updateUserManage(userManageBO);
            return;
        }
        UserManage userByAccount = this.getUserByAccount(userManageBO.getAccount());
        if (userByAccount != null) {
            throw new BaseBusinessException(400, "账号已存在,请重新输入!!");
        }
        //手机号码正则匹配
        String REGEX_MOBILE = "((\\+86|0086)?\\s*)((134[0-8]\\d{7})|(((13([0-3]|[5-9]))|(14[5-9])|15([0-3]|[5-9])|(16(2|[5-7]))|17([0-3]|[5-8])|18[0-9]|19(1|[8-9]))\\d{8})|(14(0|1|4)0\\d{7})|(1740([0-5]|[6-9]|[10-12])\\d{7}))";
        if (!Pattern.matches(REGEX_MOBILE, userManageBO.getPhoneNumber())) {
            throw new BaseBusinessException(400, "手机号码不合法");
        }
        UserManage userManage = BeanUtil.toBean(userManageBO, UserManage.class);
        String password = bCryptPasswordEncoder.encode(Base64.decodeStr(userManage.getPassword()));
        userManage.setPassword(password);
        this.save(userManage);
        //添加到单位
        unitUserService.saveUnitUser(userManage.getUserId(), userManageBO.getUnitId());
        //绑定角色
        if (userManageBO.getReviewAuthority() != null && userManageBO.getReviewAuthority() == 1) {
            userManageBO.getRoleIds().add("4");
        }
        roleUserService.addRole(userManageBO.getRoleIds(), userManage.getUserId());

    }

    @Override
    public void updateUserManage(UserManageBO userManageBO) {
        if (StrUtil.isBlank(userManageBO.getUserId())) {
            throw new BaseBusinessException(400, "用户id不许为空");
        }
        UserManage userManage = BeanUtil.toBean(userManageBO, UserManage.class);
        if (StrUtil.isNotBlank(userManageBO.getPassword())) {
            userManage.setPassword(bCryptPasswordEncoder.encode(Base64.decodeStr(userManageBO.getPassword())));
        }
        this.updateById(userManage);
        //角色先删后增
        roleUserService.removeRole(userManage.getUserId());
        if (userManageBO.getReviewAuthority() == 1) {
            userManageBO.getRoleIds().add("4");
        }
        roleUserService.addRole(userManageBO.getRoleIds(), userManage.getUserId());
        //添加到单位
        unitUserService.saveUnitUser(userManage.getUserId(), userManageBO.getUnitId());
    }

    @Override
    public void deleteUserManage(String userId) {
        if (Objects.requireNonNull(LoginUserUtil.getUserId()).equals(userId)) {
            throw new BaseBusinessException(400, "不许删除当前用户");
        }
        UserManage userManage = new UserManage();
        userManage.setUserId(userId);
        userManage.setDel(1);
        this.updateById(userManage);
        roleUserService.removeRole(userId);
        unitUserService.removeUserByUserId(userId);
    }

    @Override
    public void clearUserAuthorityInfoByMenuId(String menuId) {
        List<UserManage> sysUsers = this.baseMapper.listByMenuId(menuId);
        sysUsers.forEach(u -> {
            this.clearUserAuthorityInfo(u.getUserId());
        });
    }

    @Override
    public void clearUserAuthorityInfoByRoleId(String roleId) {
        List<UserManage> sysUsers = this.baseMapper.listByRoleId(roleId);
        sysUsers.forEach(u -> {
            this.clearUserAuthorityInfo(u.getUserId());
        });
    }


    @Override
    public void updatePassword(UpdatePasswordVO updatePasswordVO) {
        //验证旧密码
        String password = Base64.decodeStr(updatePasswordVO.getPassword());
        UserManage userManage = this.getById(LoginUserUtil.getUserId());
        if (!bCryptPasswordEncoder.matches(password, userManage.getPassword())) {
            throw new BaseBusinessException(400, "原密码输入不正确!");
        }
        String newPassword = Base64.decodeStr(updatePasswordVO.getNewPassword());
        String towPassword = Base64.decodeStr(updatePasswordVO.getTowPassword());
        if (!newPassword.equals(towPassword)) {
            throw new BaseBusinessException(400, "两次输入的密码不一致!");
        }
        if (password.equals(newPassword)) {
            throw new BaseBusinessException(400, "不能与原密码一致!!");
        }
        //校验密码复杂度
        PwdCheckUtil.checkStrongPwd(newPassword);
        newPassword = bCryptPasswordEncoder.encode(newPassword);
        UserManage user = new UserManage();
        user.setPassword(newPassword);
        user.setUserId(LoginUserUtil.getUserId());
        this.updateById(user);
    }

    @Override
    public List<UserManage> getListByRole(String roleId) {
        return this.baseMapper.listByRoleId(roleId);
    }

    @Override
    public List<RoleManage> getRoleById(String userId) {
        return this.baseMapper.getRoleById(userId);
    }

    @Override
    public UserManage getInfo(String userId) {
        return this.baseMapper.getInfo(userId);
    }
}
