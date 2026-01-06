package com.property.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.property.system.common.Result;
import com.property.system.dto.ParkingAddDTO;
import com.property.system.dto.ParkingUpdateDTO;
import com.property.system.entity.Parking;

public interface ParkingService {
    Result<Void> add(ParkingAddDTO dto);
    Result<IPage<Parking>> getUserParkings(String houseNumber, Integer pageNum, Integer pageSize);
    Result<IPage<Parking>> getAllParkings(Integer pageNum, Integer pageSize);
    Result<Void> update(ParkingUpdateDTO dto);
    Result<Void> delete(Long parkingId);
}