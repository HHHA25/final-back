package com.property.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.system.common.Result;
import com.property.system.common.exception.BusinessException;
import com.property.system.dto.HouseAddDTO;
import com.property.system.dto.HouseUpdateDTO;
import com.property.system.entity.Building;
import com.property.system.entity.House;
import com.property.system.mapper.BuildingMapper;
import com.property.system.mapper.HouseMapper;
import com.property.system.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HouseServiceImpl extends ServiceImpl<HouseMapper, House> implements HouseService {

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private BuildingMapper buildingMapper;

    @Override
    public Result<Void> add(HouseAddDTO dto) {
        // 检查楼栋是否存在
        Building building = buildingMapper.selectById(dto.getBuildingId());
        if (building == null) {
            throw new BusinessException("楼栋不存在");
        }

        // 检查房号是否重复（在同一楼栋内）
        House existHouse = lambdaQuery()
                .eq(House::getBuildingId, dto.getBuildingId())
                .eq(House::getHouseNumber, dto.getHouseNumber())
                .one();
        if (existHouse != null) {
            throw new BusinessException("该楼栋下房号已存在");
        }

        House house = new House();
        house.setBuildingId(dto.getBuildingId());
        house.setHouseNumber(dto.getHouseNumber());
        house.setFloor(dto.getFloor());
        house.setUnitType(dto.getUnitType());
        house.setArea(dto.getArea());
        house.setHouseStatus(dto.getHouseStatus() != null ? dto.getHouseStatus() : "VACANT");
        house.setOwnerName(dto.getOwnerName());
        house.setOwnerPhone(dto.getOwnerPhone());
        house.setOwnerIdCard(dto.getOwnerIdCard());
        house.setResidentName(dto.getResidentName());
        house.setResidentPhone(dto.getResidentPhone());
        house.setCreateTime(LocalDateTime.now());

        houseMapper.insert(house);
        return Result.success();
    }

    @Override
    public Result<Void> update(HouseUpdateDTO dto) {
        House house = houseMapper.selectById(dto.getId());
        if (house == null) {
            throw new BusinessException("房屋不存在");
        }

        // 检查楼栋是否存在
        Building building = buildingMapper.selectById(dto.getBuildingId());
        if (building == null) {
            throw new BusinessException("楼栋不存在");
        }

        // 检查房号是否被其他房屋使用（在同一楼栋内）
        House existHouse = lambdaQuery()
                .eq(House::getBuildingId, dto.getBuildingId())
                .eq(House::getHouseNumber, dto.getHouseNumber())
                .ne(House::getId, dto.getId())
                .one();
        if (existHouse != null) {
            throw new BusinessException("该楼栋下房号已被其他房屋使用");
        }

        house.setBuildingId(dto.getBuildingId());
        house.setHouseNumber(dto.getHouseNumber());
        house.setFloor(dto.getFloor());
        house.setUnitType(dto.getUnitType());
        house.setArea(dto.getArea());
        house.setHouseStatus(dto.getHouseStatus());
        house.setOwnerName(dto.getOwnerName());
        house.setOwnerPhone(dto.getOwnerPhone());
        house.setOwnerIdCard(dto.getOwnerIdCard());
        house.setResidentName(dto.getResidentName());
        house.setResidentPhone(dto.getResidentPhone());
        house.setUpdateTime(LocalDateTime.now());


        houseMapper.updateById(house);
        return Result.success();
    }

    @Override
    public Result<Void> delete(Long houseId) {
        House house = houseMapper.selectById(houseId);
        if (house == null) {
            throw new BusinessException("房屋不存在");
        }

        houseMapper.deleteById(houseId);
        return Result.success();
    }

    @Override
    public Result<IPage<House>> getHousesByBuilding(Long buildingId, Integer pageNum, Integer pageSize) {
        Page<House> page = new Page<>(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);
        IPage<House> housePage = houseMapper.selectByBuildingId(page, buildingId);
        return Result.success(housePage);
    }

    @Override
    public Result<IPage<House>> getAllHouses(Integer pageNum, Integer pageSize) {
        Page<House> page = new Page<>(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);
        IPage<House> housePage = houseMapper.selectAllWithBuilding(page);
        return Result.success(housePage);
    }

    @Override
    public Result<IPage<House>> searchHouses(String keyword, String houseNumber, Integer pageNum, Integer pageSize) {
        Page<House> page = new Page<>(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);
        IPage<House> housePage = houseMapper.searchHouses(page, keyword, houseNumber);
        return Result.success(housePage);
    }

    @Override
    public Result<House> getHouseDetail(Long houseId) {
        House house = houseMapper.selectById(houseId);
        if (house == null) {
            throw new BusinessException("房屋不存在");
        }
        return Result.success(house);
    }
}