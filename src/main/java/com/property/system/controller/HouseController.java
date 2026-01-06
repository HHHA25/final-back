package com.property.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.property.system.common.Result;
import com.property.system.dto.HouseAddDTO;
import com.property.system.dto.HouseUpdateDTO;
import com.property.system.entity.House;
import com.property.system.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/house")
@Validated
public class HouseController {

    @Autowired
    private HouseService houseService;

    @PostMapping("/admin/add")
    public Result<Void> add(@Valid @RequestBody HouseAddDTO dto, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        return houseService.add(dto);
    }

    @PutMapping("/admin/update")
    public Result<Void> update(@Valid @RequestBody HouseUpdateDTO dto, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        return houseService.update(dto);
    }

    @DeleteMapping("/admin/delete/{houseId}")
    public Result<Void> delete(@PathVariable Long houseId, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        return houseService.delete(houseId);
    }

    @GetMapping("/building/{buildingId}")
    public Result<IPage<House>> getHousesByBuilding(
            @PathVariable Long buildingId,
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize) {
        return houseService.getHousesByBuilding(buildingId, pageNum, pageSize);
    }

    @GetMapping("/admin/all")
    public Result<IPage<House>> getAllHouses(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.forbidden();
        }
        return houseService.getAllHouses(pageNum, pageSize);
    }

    @GetMapping("/search")
    public Result<IPage<House>> searchHouses(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String houseNumber,
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize) {
        return houseService.searchHouses(keyword, houseNumber, pageNum, pageSize);
    }

    @GetMapping("/{houseId}")
    public Result<House> getHouseDetail(@PathVariable Long houseId) {
        return houseService.getHouseDetail(houseId);
    }

    @GetMapping("/by-number/{houseNumber}")
    public Result<House> getHouseByNumber(@PathVariable String houseNumber) {
        House house = houseService.lambdaQuery()
                .eq(House::getHouseNumber, houseNumber)
                .one();
        if (house == null) {
            return Result.fail(404, "房屋不存在");
        }
        return Result.success(house);
    }
}