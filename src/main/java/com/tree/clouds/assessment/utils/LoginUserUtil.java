package com.tree.clouds.assessment.utils;

import com.tree.clouds.assessment.model.entity.UserManage;
import com.tree.clouds.assessment.security.JwtAuthenticationFilter;

/**
 * @author 林振坤
 * @description
 * @date 2022/1/2 0002 18:32
 */
public class LoginUserUtil {

    public static String getUserId() {
        UserManage userManage = JwtAuthenticationFilter.getLoginUser();
        if (userManage == null) {
            return null;
        } else {
            return userManage.getUserId();
        }
    }

    public static String getUserName() {
        UserManage userManage = JwtAuthenticationFilter.getLoginUser();
        if (userManage == null) {
            return null;
        } else {
            return userManage.getUserName();
        }
    }

    public static String getUnitId() {
        UserManage userManage = JwtAuthenticationFilter.getLoginUser();
        if (userManage == null) {
            return null;
        } else {
            return userManage.getUnitId();
        }
    }

    public static String getUserAccount() {
        UserManage userManage = JwtAuthenticationFilter.getLoginUser();
        if (userManage == null) {
            return null;
        } else {
            return userManage.getAccount();
        }
    }

}
