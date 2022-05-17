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
        try{
            Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
            if (ObjectUtil.isNull(credentials) || StrUtil.isBlank(credentials.toString())) {
                return null;
            }
            UserManage user = (UserManage) credentials;
            return user.getUserId();
        }catch (RuntimeException e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getUserName() {
        Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
        if (ObjectUtil.isNull(credentials) || StrUtil.isBlank(credentials.toString())) {
            return null;
        }
        UserManage user = (UserManage) credentials;
        return user.getUserName();
    }

    public static String getUnitId() {
        Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
        if (ObjectUtil.isNull(credentials) || StrUtil.isBlank(credentials.toString())) {
            return null;
        }
        UserManage user = (UserManage) credentials;
        return user.getUnitId();
    }

    public static String getUserAccount() {
        Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
        if (ObjectUtil.isNull(credentials) || StrUtil.isBlank(credentials.toString())) {
            return null;
        }
        UserManage user = (UserManage) credentials;
        return user.getAccount();
    }

}
