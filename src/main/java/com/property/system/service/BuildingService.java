package com.property.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.property.system.common.Result;
import com.property.system.dto.BuildingAddDTO;
import com.property.system.dto.BuildingUpdateDTO;
import com.property.system.entity.Building;

public interface BuildingService extends IService<Building> {
    Result<Void> add(BuildingAddDTO dto);
    Result<Void> update(BuildingUpdateDTO dto);
    Result<Void> delete(Long buildingId);
    Result<IPage<Building>> getAllBuildings(Integer pageNum, Integer pageSize);
    Result<Building> getBuildingDetail(Long buildingId);
}