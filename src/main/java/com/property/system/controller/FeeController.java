// 路径：src/main/java/com/property/system/controller/FeeController.java
package com.property.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.property.system.common.Result;
import com.property.system.dto.FeeAddDTO;
import com.property.system.dto.FeePayDTO;
import com.property.system.entity.Fee;
import com.property.system.entity.User;
import com.property.system.service.FeeService;
import com.property.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * 物业费控制器
 */
@RestController
@RequestMapping("/api/fee")
@Validated
public class FeeController {

    @Autowired
    private FeeService feeService;

    @Autowired
    private UserService userService;

    /**
     * 管理员添加物业费
     * POST /api/fee/admin/add
     */
    @PostMapping("/admin/add")
    public Result<Void> add(@Valid @RequestBody FeeAddDTO dto, HttpServletRequest request) {
        // 校验管理员权限
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        return feeService.add(dto);
    }

    /**
     * 查询用户的物业费
     * GET /api/fee/my?userId=1&pageNum=1
     */
    @GetMapping("/my")
    public Result<IPage<Fee>> getMyFees(
            @RequestParam String houseNumber,
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            HttpServletRequest request) {
        // 校验权限
        String username = (String) request.getAttribute("username");
        User user = userService.getByUsername(username);
        // 管理员可以查询所有，居民只能查自己的
        if (!"ADMIN".equals(user.getRole()) && !user.getHouseNumber().equals(houseNumber)) {
            return Result.forbidden();
        }
        return feeService.getUserFees(houseNumber, pageNum, pageSize);
    }

    /**
     * 管理员查询所有物业费
     * GET /api/fee/admin/all?pageNum=1
     */
    @GetMapping("/admin/all")
    public Result<IPage<Fee>> getAllFees(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            HttpServletRequest request) {
        // 校验管理员权限
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        return feeService.getAllFees(pageNum, pageSize);
    }

    /**
     * 缴纳物业费
     * PUT /api/fee/pay
     */
    @PutMapping("/pay")
    public Result<Void> pay(@Valid @RequestBody FeePayDTO dto, HttpServletRequest request) {
        // 获取当前用户房号
        String username = (String) request.getAttribute("username");
        User user = userService.getByUsername(username);
        return feeService.pay(dto, user.getHouseNumber());
    }

    // 添加搜索方法
    @GetMapping("/admin/search")
    public Result<IPage<Fee>> searchFees(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String houseNumber,
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        return feeService.searchFees(keyword, houseNumber, pageNum, pageSize);
    }
}