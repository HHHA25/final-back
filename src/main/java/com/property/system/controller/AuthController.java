package com.property.system.controller;

import com.property.system.common.Result;
import com.property.system.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 刷新Token
     * GET /api/auth/refresh
     */
    @GetMapping("/refresh")
    public Result<String> refreshToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            return Result.fail(401, "Token不能为空");
        }

        try {
            if (jwtUtils.isExpired(token)) {
                return Result.fail(401, "Token已过期，请重新登录");
            }

            String newToken = jwtUtils.renewToken(token);
            return Result.success(newToken);
        } catch (Exception e) {
            return Result.fail(401, "Token无效");
        }
    }
}