package com.tree.clouds.assessment.security;

import cn.hutool.core.util.StrUtil;
import com.tree.clouds.assessment.common.Constants;
import com.tree.clouds.assessment.model.entity.UserManage;
import com.tree.clouds.assessment.service.UserManageService;
import com.tree.clouds.assessment.utils.BaseBusinessException;
import com.tree.clouds.assessment.utils.JwtUtils;
import com.tree.clouds.assessment.utils.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.security.auth.login.AccountExpiredException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailServiceImpl userDetailService;
    @Autowired
    private UserManageService userManageService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String jwt = request.getHeader(jwtUtils.getHeader());
        if (StrUtil.isBlankOrUndefined(jwt)) {
            chain.doFilter(request, response);
            return;
        }

        Claims claim = jwtUtils.getClaimByToken(jwt);
        if (claim == null) {
            throw new JwtException("token 异常");
        }
        if (jwtUtils.isTokenExpired(claim)) {
            throw new JwtException("token已过期");
        }

        String account = claim.getSubject();
        String key = null;
        try {
            if (claim.containsKey("key")) {
                key = claim.get("key").toString();
                Object hget = redisUtil.hget(Constants.ACCOUNT_KEY, account);
                if (hget != null && !hget.equals(key)) {
                    jwtAuthenticationEntryPoint.commence(request, response, new InsufficientAuthenticationException("用户账户在其他地区登录，可能存在账号被盗风险!", new BaseBusinessException(402, "")));
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        Object hget = redisUtil.hget(Constants.ACCOUNT_KEY, account);
        if (hget != null && !hget.equals(key)) {
            jwtAuthenticationEntryPoint.commence(request, response, new InsufficientAuthenticationException("用户账户在其他地区登录，可能存在账号被盗风险!", new BaseBusinessException(402, "")));
        }
        // 获取用户的权限等信息
        UserManage userManage = userManageService.getUserByAccount(account);
        if (userManage == null) {
            throw new JwtException("用户不存在!");
        }
        userManage = userManageService.getInfo(userManage.getUserId());
        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(userManage.getUserName(), userManage, userDetailService.getUserAuthority(userManage.getUserId()));

        SecurityContextHolder.getContext().setAuthentication(token);
        //重新签发token
        // 生成jwt，并放置到请求头中
        String netToken = jwtUtils.generateToken(userManage.getAccount(), key);
        response.setHeader(jwtUtils.getHeader(), netToken);
        chain.doFilter(request, response);
    }

}
