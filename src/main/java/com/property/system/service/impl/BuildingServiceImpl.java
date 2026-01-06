package com.property.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.system.common.Result;
import com.property.system.common.exception.BusinessException;
import com.property.system.dto.BuildingAddDTO;
import com.property.system.dto.BuildingUpdateDTO;
import com.property.system.entity.Building;
import com.property.system.mapper.BuildingMapper;
import com.property.system.mapper.HouseMapper;
import com.property.system.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BuildingServiceImpl extends ServiceImpl<BuildingMapper, Building> implements BuildingService {

    @Autowired
    private BuildingMapper buildingMapper;

    @Autowired
    private HouseMapper houseMapper;

    @Override
    public Result<Void> add(BuildingAddDTO dto) {
        // 检查楼栋编号是否重复
        Building existBuilding = lambdaQuery()
                .eq(Building::getBuildingNumber, dto.getBuildingNumber())
                .one();
        if (existBuilding != null) {
            throw new BusinessException("楼栋编号已存在");
        }

        Building building = new Building();
        building.setBuildingNumber(dto.getBuildingNumber());
        building.setBuildingName(dto.getBuildingName());
        building.setTotalFloors(dto.getTotalFloors());
        building.setTotalUnits(dto.getTotalUnits());
        building.setBuildingType(dto.getBuildingType());
        building.setStatus("ACTIVE");
        building.setCreateTime(LocalDateTime.now());

        if (dto.getCompletionDate() != null && !dto.getCompletionDate().isEmpty()) {
            building.setCompletionDate(LocalDateTime.parse(dto.getCompletionDate() + "T00:00:00"));
        }

        buildingMapper.insert(building);
        return Result.success();
    }

    @Override
    public Result<Void> update(BuildingUpdateDTO dto) {
        Building building = buildingMapper.selectById(dto.getId());
        if (building == null) {
            throw new BusinessException("楼栋不存在");
        }

        // 检查楼栋编号是否被其他楼栋使用
        Building existBuilding = lambdaQuery()
                .eq(Building::getBuildingNumber, dto.getBuildingNumber())
                .ne(Building::getId, dto.getId())
                .one();
        if (existBuilding != null) {
            throw new BusinessException("楼栋编号已被其他楼栋使用");
        }

        building.setBuildingNumber(dto.getBuildingNumber());
        building.setBuildingName(dto.getBuildingName());
        building.setTotalFloors(dto.getTotalFloors());
        building.setTotalUnits(dto.getTotalUnits());
        building.setBuildingType(dto.getBuildingType());
        building.setStatus(dto.getStatus());
        building.setUpdateTime(LocalDateTime.now());

        if (dto.getCompletionDate() != null && !dto.getCompletionDate().isEmpty()) {
            building.setCompletionDate(LocalDateTime.parse(dto.getCompletionDate() + "T00:00:00"));
        }

        buildingMapper.updateById(building);
        return Result.success();
    }

    @Override
    public Result<Void> delete(Long buildingId) {
        Building building = buildingMapper.selectById(buildingId);
        if (building == null) {
            throw new BusinessException("楼栋不存在");
        }

        // 检查楼栋下是否有房屋
        Integer houseCount = buildingMapper.countHousesByBuilding(buildingId);
        if (houseCount > 0) {
            throw new BusinessException("该楼栋下存在房屋，无法删除");
        }

        buildingMapper.deleteById(buildingId);
        return Result.success();
    }

    @Override
    public Result<IPage<Building>> getAllBuildings(Integer pageNum, Integer pageSize) {
        Page<Building> page = new Page<>(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);
        IPage<Building> buildingPage = buildingMapper.selectAll(page);
        return Result.success(buildingPage);
    }

    @Override
    public Result<Building> getBuildingDetail(Long buildingId) {
        Building building = buildingMapper.selectById(buildingId);
        if (building == null) {
            throw new BusinessException("楼栋不存在");
        }

        // 统计房屋信息
        Integer totalHouses = buildingMapper.countHousesByBuilding(buildingId);
        Integer occupiedHouses = buildingMapper.countOccupiedHouses(buildingId);

        // 可以设置到building的扩展字段中，或者单独返回统计信息
        return Result.success(building);
    }
}