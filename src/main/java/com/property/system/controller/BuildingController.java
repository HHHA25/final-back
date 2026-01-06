package com.property.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.property.system.common.Result;
import com.property.system.dto.BuildingAddDTO;
import com.property.system.dto.BuildingUpdateDTO;
import com.property.system.entity.Building;
import com.property.system.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/building")
@Validated
public class BuildingController {

    @Autowired
    private BuildingService buildingService;

    @PostMapping("/admin/add")
    public Result<Void> add(@Valid @RequestBody BuildingAddDTO dto, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        return buildingService.add(dto);
    }

    @PutMapping("/admin/update")
    public Result<Void> update(@Valid @RequestBody BuildingUpdateDTO dto, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        return buildingService.update(dto);
    }

    @DeleteMapping("/admin/delete/{buildingId}")
    public Result<Void> delete(@PathVariable Long buildingId, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        return buildingService.delete(buildingId);
    }

    @GetMapping("/admin/all")
    public Result<IPage<Building>> getAllBuildings(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        return buildingService.getAllBuildings(pageNum, pageSize);
    }

    @GetMapping("/{buildingId}")
    public Result<Building> getBuildingDetail(@PathVariable Long buildingId) {
        return buildingService.getBuildingDetail(buildingId);
    }
}