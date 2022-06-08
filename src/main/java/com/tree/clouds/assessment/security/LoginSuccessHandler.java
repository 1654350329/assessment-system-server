package com.tree.clouds.assessment.security;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.tree.clouds.assessment.common.Constants;
import com.tree.clouds.assessment.common.RestResponse;
import com.tree.clouds.assessment.model.entity.UserManage;
import com.tree.clouds.assessment.service.UserManageService;
import com.tree.clouds.assessment.utils.JwtUtils;
import com.tree.clouds.assessment.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserManageService userManageService;
    @Autowired
    private UserDetailServiceImpl userDetailService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        //登入日志
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        //清除登入失败记录
        redisUtil.hdel(Constants.ERROR_LOGIN, username);
        UserManage userByAccount = userManageService.getUserByAccount(username);
        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(userByAccount.getAccount(), userByAccount, userDetailService.getUserAuthority(userByAccount.getUserId()));
        SecurityContextHolder.getContext().setAuthentication(token);

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();

        // 生成jwt，并放置到请求头中
        String jwt = jwtUtils.generateToken(authentication.getName());
        response.setHeader(jwtUtils.getHeader(), jwt);
        Map<String, Object> map = new HashMap<>();
        map.put(jwtUtils.getHeader(), jwt);
        if (StrUtil.isNotBlank(userByAccount.getPasswordTime()) &&
                DateUtil.parseDate(userByAccount.getPasswordTime()).getTime() + 1000 * 60 * 60 * 24 * 85 > new Date().getTime()) {
            map.put("passwordStatus", "密码长时间未修改,请及时修改密码!");
        }
        if (password.equals("888888")) {
            map.put("passwordStatus", "请及时修改初始密码!");
        }
        RestResponse result = RestResponse.ok(map);

        outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));

        outputStream.flush();
        outputStream.close();
    }

}
