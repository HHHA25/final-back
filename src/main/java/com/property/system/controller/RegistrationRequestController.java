package com.property.system.controller;

import com.property.system.common.Result;
import com.property.system.entity.RegistrationRequest;
import com.property.system.service.RegistrationRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/registration")
public class RegistrationRequestController {

    @Autowired
    private RegistrationRequestService registrationRequestService;

    @GetMapping("/admin/pending")
    public Result<List<RegistrationRequest>> getPendingRequests(HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        return registrationRequestService.getPendingRequests();
    }

    @PostMapping("/admin/approve/{requestId}")
    public Result<Void> approveRegistration(@PathVariable Long requestId, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        return registrationRequestService.approveRegistration(requestId);
    }

    @PostMapping("/admin/reject/{requestId}")
    public Result<Void> rejectRegistration(@PathVariable Long requestId, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        return registrationRequestService.rejectRegistration(requestId);
    }

    @GetMapping("/admin/pending-count")
    public Result<Integer> getPendingCount(HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        return Result.success(registrationRequestService.getPendingCount());
    }

    @GetMapping("/admin/processed")
    public Result<List<RegistrationRequest>> getProcessedRequests(HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        return registrationRequestService.getProcessedRequests();
    }
}