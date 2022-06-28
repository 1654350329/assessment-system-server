package com.tree.clouds.assessment.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.tree.clouds.assessment.model.entity.UserManage;
import com.tree.clouds.assessment.security.JwtAuthenticationFilter;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author 林振坤
 * @description
 * @date 2022/1/2 0002 18:32
 */
public class LoginUserUtil {

    public static String getUserId() {
        UserManage user = getUserManage();
        if (user == null) return null;
        return user.getUserId();
    }

    public static String getUserName() {
        UserManage user = getUserManage();
        if (user == null) return null;
        return user.getUserName();
    }

    public static String getUnitId() {
        UserManage user = getUserManage();
        if (user == null) return null;
        return user.getUnitId();
    }

    public static String getUnitName() {
        UserManage user = getUserManage();
        if (user == null) return null;
        return user.getUnitName();
    }

    public static String getUserAccount() {
        UserManage user = getUserManage();
        if (user == null) return null;
        return user.getAccount();
    }

    private static UserManage getUserManage() {
        try {
            if (SecurityContextHolder.getContext() == null || SecurityContextHolder.getContext().getAuthentication() == null) {
                return null;
            }
            Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
            if (ObjectUtil.isNull(credentials) || StrUtil.isBlank(credentials.toString())) {
                return null;
            }
            return (UserManage) credentials;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return null;
        }
    }
}
