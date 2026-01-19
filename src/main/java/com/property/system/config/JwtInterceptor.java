package com.property.system.config;

import com.alibaba.fastjson.JSON;
import com.property.system.common.Result;
import com.property.system.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JWT拦截器：验证Token是否有效
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;// 请求头参数名

    @Value("${jwt.header}")
    private String tokenHeader;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 允许跨域
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Content-Type", "application/json;charset=UTF-8");

        // 获取请求头中的Token
        String token = request.getHeader(tokenHeader);
        if (token == null || token.isEmpty()) {
            // 无Token，返回未登录
            response.getWriter().write(JSON.toJSONString(Result.unAuth()));
            return false;
        }

        // 验证Token
        try {
            // 检查是否过期
            if (jwtUtils.isExpired(token)) {
                response.getWriter().write(JSON.toJSONString(Result.fail(401, "Token已过期，请重新登录")));
                return false;
            }

            // Token有效，将用户信息存入请求属性
            String username = jwtUtils.getUsername(token);
            String role = jwtUtils.getRole(token);
            request.setAttribute("username", username);
            request.setAttribute("role", role);

            // 检查是否需要续期，如果需要，在响应头中添加新Token
            if (jwtUtils.shouldRenew(token)) {
                String newToken = jwtUtils.renewToken(token);
                response.setHeader("X-Renew-Token", newToken);
            }

            return true;  // 放行
        } catch (Exception e) {
            // Token无效
            response.getWriter().write(JSON.toJSONString(Result.fail(401, "Token无效，请重新登录")));
            return false;
        }
    }
}