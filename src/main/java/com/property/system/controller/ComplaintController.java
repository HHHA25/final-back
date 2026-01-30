package com.property.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.property.system.common.Result;
import com.property.system.dto.ComplaintSubmitDTO;
import com.property.system.dto.ComplaintUpdateDTO;
import com.property.system.entity.Complaint;
import com.property.system.entity.User;
import com.property.system.service.ComplaintService;
import com.property.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/complaint")
@Validated
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private UserService userService;

    @PostMapping("/submit")
    public Result<Void> submit(@Valid @RequestBody ComplaintSubmitDTO dto, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        User user = userService.getByUsername(username);

        // 居民只能提交自己房号的投诉
        if (!user.getHouseNumber().equals(dto.getHouseNumber())) {
            return Result.forbidden();
        }

        return complaintService.submit(dto);
    }

    @GetMapping("/my")
    public Result<IPage<Complaint>> getMyComplaints(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        User user = userService.getByUsername(username);
        return complaintService.getUserComplaints(user.getHouseNumber(), pageNum, pageSize);
    }

    @GetMapping("/admin/all")
    public Result<IPage<Complaint>> getAllComplaints(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        return complaintService.getAllComplaints(pageNum, pageSize);
    }

    @PutMapping("/admin/update")
    public Result<Void> updateStatus(@Valid @RequestBody ComplaintUpdateDTO dto, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        return complaintService.updateStatus(dto);
    }

    // 添加搜索方法
    @GetMapping("/admin/search")
    public Result<IPage<Complaint>> searchComplaints(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String houseNumber,
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        return complaintService.searchComplaints(keyword, houseNumber, pageNum, pageSize);
    }

    // 添加居民搜索方法
    @GetMapping("/my/search")
    public Result<IPage<Complaint>> searchMyComplaints(
            @RequestParam String houseNumber,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        User user = userService.getByUsername(username);

        // 检查权限：居民只能搜索自己的投诉
        if (!user.getHouseNumber().equals(houseNumber)) {
            return Result.forbidden();
        }

        return complaintService.searchComplaints(keyword, houseNumber, pageNum, pageSize);
    }
}