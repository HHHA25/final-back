package com.property.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.system.entity.Parking;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ParkingMapper extends BaseMapper<Parking> {
    @Select("SELECT * FROM parking WHERE house_number = #{houseNumber} ORDER BY start_time DESC")
    IPage<Parking> selectByHouseNumber(Page<Parking> page, @Param("houseNumber") String houseNumber);

    @Select("SELECT * FROM parking ORDER BY parking_number ASC")
    IPage<Parking> selectAll(Page<Parking> page);
}