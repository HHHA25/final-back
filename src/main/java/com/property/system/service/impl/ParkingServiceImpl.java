package com.property.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.system.common.Result;
import com.property.system.common.exception.BusinessException;
import com.property.system.dto.ParkingAddDTO;
import com.property.system.dto.ParkingUpdateDTO;
import com.property.system.entity.Parking;
import com.property.system.mapper.ParkingMapper;
import com.property.system.service.ParkingService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ParkingServiceImpl extends ServiceImpl<ParkingMapper, Parking> implements ParkingService {

    @Override
    public Result<Void> add(ParkingAddDTO dto) {
        // 检查车位号是否已存在
        Parking existParking = lambdaQuery()
                .eq(Parking::getParkingNumber, dto.getParkingNumber())
                .one();
        if (existParking != null) {
            throw new BusinessException("车位号已存在");
        }

        Parking parking = new Parking();
        parking.setParkingNumber(dto.getParkingNumber());
        parking.setHouseNumber(dto.getHouseNumber());
        parking.setResidentName(dto.getResidentName());
        parking.setCarPlate(dto.getCarPlate());
        parking.setStatus(dto.getStatus());
        parking.setStartTime(LocalDateTime.now());

        baseMapper.insert(parking);
        return Result.success();
    }

    @Override
    public Result<IPage<Parking>> getUserParkings(String houseNumber, Integer pageNum, Integer pageSize) {
        Page<Parking> page = new Page<>(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);
        IPage<Parking> parkingPage = baseMapper.selectByHouseNumber(page, houseNumber);
        return Result.success(parkingPage);
    }

    @Override
    public Result<IPage<Parking>> getAllParkings(Integer pageNum, Integer pageSize) {
        Page<Parking> page = new Page<>(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);
        IPage<Parking> parkingPage = baseMapper.selectAll(page);
        return Result.success(parkingPage);
    }

    @Override
    public Result<Void> update(ParkingUpdateDTO dto) {
        Parking parking = baseMapper.selectById(dto.getParkingId());
        if (parking == null) {
            throw new BusinessException("车位记录不存在");
        }

        // 检查车位号是否被其他记录使用
        Parking existParking = lambdaQuery()
                .eq(Parking::getParkingNumber, dto.getParkingNumber())
                .ne(Parking::getId, dto.getParkingId())
                .one();
        if (existParking != null) {
            throw new BusinessException("车位号已被其他记录使用");
        }

        parking.setParkingNumber(dto.getParkingNumber());
        parking.setHouseNumber(dto.getHouseNumber());
        parking.setResidentName(dto.getResidentName());
        parking.setCarPlate(dto.getCarPlate());
        parking.setStatus(dto.getStatus());

        baseMapper.updateById(parking);
        return Result.success();
    }

    @Override
    public Result<Void> delete(Long parkingId) {
        Parking parking = baseMapper.selectById(parkingId);
        if (parking == null) {
            throw new BusinessException("车位记录不存在");
        }

        baseMapper.deleteById(parkingId);
        return Result.success();
    }

    @Override
    public Result<IPage<Parking>> searchParkings(String keyword, String houseNumber, Integer pageNum, Integer pageSize) {
        Page<Parking> page = new Page<>(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);

        LambdaQueryWrapper<Parking> queryWrapper = new LambdaQueryWrapper<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            queryWrapper.and(wrapper ->
                    wrapper.like(Parking::getParkingNumber, keyword)
                            .or()
                            .like(Parking::getHouseNumber, keyword));
        }

        if (houseNumber != null && !houseNumber.trim().isEmpty()) {
            queryWrapper.like(Parking::getHouseNumber, houseNumber);
        }

        queryWrapper.orderByDesc(Parking::getStartTime);

        IPage<Parking> parkingPage = baseMapper.selectPage(page, queryWrapper);
        return Result.success(parkingPage);
    }
}