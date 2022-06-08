package com.tree.clouds.assessment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.mapper.SysRoleMenuMapper;
import com.tree.clouds.assessment.model.bo.UserManageBO;
import com.tree.clouds.assessment.model.entity.RoleManage;
import com.tree.clouds.assessment.model.entity.SysMenu;
import com.tree.clouds.assessment.model.entity.UserManage;
import com.tree.clouds.assessment.mapper.UserManageMapper;
import com.tree.clouds.assessment.model.vo.UpdatePasswordVO;
import com.tree.clouds.assessment.model.vo.UserManagePageVO;
import com.tree.clouds.assessment.service.UnitUserService;
import com.tree.clouds.assessment.service.RoleUserService;
import com.tree.clouds.assessment.service.SysMenuService;
import com.tree.clouds.assessment.service.UserManageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.assessment.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Override
    public void rebuildPassword(List<String> ids) {
        // 加密后密码
        String password = bCryptPasswordEncoder.encode("888888");

        List<UserManage> userManages = this.listByIds(ids);
        userManages.forEach(userManage -> userManage.setPassword(password));
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
        if (StrUtil.isNotBlank(userManageBO.getUserId())) {
            updateUserManage(userManageBO);
            return;
        }
        UserManage userByAccount = this.getUserByAccount(userManageBO.getAccount());
        if (userByAccount != null) {
            throw new BaseBusinessException(400, "账号已存在,请重新输入!!");
        }
        //手机号码正则匹配
//        String REGEX_MOBILE = "((\\+86|0086)?\\s*)((134[0-8]\\d{7})|(((13([0-3]|[5-9]))|(14[5-9])|15([0-3]|[5-9])|(16(2|[5-7]))|17([0-3]|[5-8])|18[0-9]|19(1|[8-9]))\\d{8})|(14(0|1|4)0\\d{7})|(1740([0-5]|[6-9]|[10-12])\\d{7}))";
//        if (!Pattern.matches(REGEX_MOBILE, userManageBO.getPhoneNumber())) {
//            throw new BaseBusinessException(400, "手机号码不合法");
//        }
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
    }

    @Override
    public void deleteUserManage(String userId) {
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
        String decodeStr = Base64.decodeStr(updatePasswordVO.getPassword());
        //校验密码复杂度
        PwdCheckUtil.checkStrongPwd(decodeStr);
        String password = bCryptPasswordEncoder.encode(decodeStr);
        UserManage user = new UserManage();
        user.setPassword(password);
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
