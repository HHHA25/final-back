package com.property.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.property.system.common.Result;
import com.property.system.dto.RepairSubmitDTO;
import com.property.system.dto.RepairUpdateDTO;
import com.property.system.entity.Repair;
import com.property.system.entity.User;
import com.property.system.service.RepairService;
import com.property.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/repair")
@Validated
public class RepairController {

    @Autowired
    private RepairService repairService;

    @Autowired
    private UserService userService;

    /**
     * 提交维修申请
     * POST /api/repair/submit
     */
    @PostMapping("/submit")
    public Result<Void> submit(@Valid @RequestBody RepairSubmitDTO dto, HttpServletRequest request) {
        // 校验权限（只能提交自己的申请）
        String username = (String) request.getAttribute("username");
        User user = userService.getByUsername(username);
        if (!user.getId().equals(dto.getUserId())) {
            return Result.forbidden();
        }
        return repairService.submit(dto);
    }

    /**
     * 查询用户的维修申请
     * GET /api/repair/my?userId=1&pageNum=1
     */
    @GetMapping("/my")
    public Result<IPage<Repair>> getMyRepairs(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            HttpServletRequest request) {
        // 校验权限
        String username = (String) request.getAttribute("username");
        User user = userService.getByUsername(username);

        // 居民只能查自己的维修记录
        return repairService.getUserRepairs(user.getId(), pageNum, pageSize);
    }

    /**
     * 管理员查询所有维修申请
     * GET /api/repair/admin/all?pageNum=1
     */
    @GetMapping("/admin/all")
    public Result<IPage<Repair>> getAllRepairs(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            HttpServletRequest request) {
        // 校验管理员权限
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        return repairService.getAllRepairs(pageNum, pageSize);
    }

    /**
     * 管理员更新维修状态
     * PUT /api/repair/admin/update
     */
    @PutMapping("/admin/update")
    public Result<Void> updateStatus(
            @Valid @RequestBody RepairUpdateDTO dto,
            HttpServletRequest request) {
        // 校验管理员权限
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        // 获取管理员ID
        String username = (String) request.getAttribute("username");
        User admin = userService.getByUsername(username);
        return repairService.updateStatus(dto, admin.getId());
    }
}