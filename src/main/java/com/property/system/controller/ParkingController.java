package com.property.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.property.system.common.Result;
import com.property.system.dto.ParkingAddDTO;
import com.property.system.dto.ParkingUpdateDTO;
import com.property.system.entity.Parking;
import com.property.system.entity.User;
import com.property.system.service.ParkingService;
import com.property.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/parking")
@Validated
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    @Autowired
    private UserService userService;

    @PostMapping("/admin/add")
    public Result<Void> add(@Valid @RequestBody ParkingAddDTO dto, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        return parkingService.add(dto);
    }

    @GetMapping("/my")
    public Result<IPage<Parking>> getMyParkings(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        User user = userService.getByUsername(username);
        return parkingService.getUserParkings(user.getHouseNumber(), pageNum, pageSize);
    }

    @GetMapping("/admin/all")
    public Result<IPage<Parking>> getAllParkings(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        return parkingService.getAllParkings(pageNum, pageSize);
    }

    @PutMapping("/admin/update")
    public Result<Void> update(@Valid @RequestBody ParkingUpdateDTO dto, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        return parkingService.update(dto);
    }

    @DeleteMapping("/admin/delete/{parkingId}")
    public Result<Void> delete(@PathVariable Long parkingId, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        return parkingService.delete(parkingId);
    }
}