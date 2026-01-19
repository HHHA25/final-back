// 路径：src/main/java/com/property/system/controller/UserController.java
package com.property.system.controller;

import com.property.system.common.Result;
import com.property.system.dto.UserCreateDTO;
import com.property.system.dto.UserLoginDTO;
import com.property.system.dto.UserRegisterDTO;
import com.property.system.entity.RegistrationRequest;
import com.property.system.entity.User;
import com.property.system.service.RegistrationRequestService;
import com.property.system.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

/**
 * 用户控制器：登录、注册接口
 */
@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RegistrationRequestService registrationRequestService;

    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        return userService.login(loginDTO);
    }

    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        return registrationRequestService.submitRegistration(registerDTO);
    }

    @GetMapping("/admin/registration-requests")
    public Result<List<RegistrationRequest>> getPendingRequests(HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        return registrationRequestService.getPendingRequests();
    }

    @PostMapping("/admin/approve-registration")
    public Result<Void> approveRegistration(@RequestParam Long requestId, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        return registrationRequestService.approveRegistration(requestId);
    }

    @PostMapping("/admin/reject-registration")
    public Result<Void> rejectRegistration(@RequestParam Long requestId, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        return registrationRequestService.rejectRegistration(requestId);
    }
    // 管理员获取所有用户
    @GetMapping("/admin/all")
    public Result<List<User>> getAllUsers(HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        List<User> users = userService.list();
        return Result.success(users);
    }

    // 管理员删除用户
    @DeleteMapping("/admin/delete/{userId}")
    public Result<Void> deleteUser(@PathVariable Long userId, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        // 不能删除自己
        String username = (String) request.getAttribute("username");
        User currentUser = userService.getByUsername(username);
        if (currentUser.getId().equals(userId)) {
            return Result.fail(400, "不能删除自己的账号");
        }

        userService.removeById(userId);
        return Result.success();
    }
    /**
     * 管理员直接创建用户
     * POST /api/user/admin/create
     */
    @PostMapping("/admin/create")
    public Result<Void> createUser(@Valid @RequestBody UserCreateDTO dto, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        return userService.createUser(dto);
    }
}