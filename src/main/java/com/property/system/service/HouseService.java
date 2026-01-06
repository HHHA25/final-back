package com.property.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.property.system.common.Result;
import com.property.system.dto.HouseAddDTO;
import com.property.system.dto.HouseUpdateDTO;
import com.property.system.entity.House;

public interface HouseService extends IService<House> {
    Result<Void> add(HouseAddDTO dto);
    Result<Void> update(HouseUpdateDTO dto);
    Result<Void> delete(Long houseId);
    Result<IPage<House>> getHousesByBuilding(Long buildingId, Integer pageNum, Integer pageSize);
    Result<IPage<House>> getAllHouses(Integer pageNum, Integer pageSize);
    Result<IPage<House>> searchHouses(String keyword, String houseNumber, Integer pageNum, Integer pageSize);
    Result<House> getHouseDetail(Long houseId);
}