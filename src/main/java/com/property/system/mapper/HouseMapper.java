package com.property.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.system.entity.House;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface HouseMapper extends BaseMapper<House> {

    @Select("SELECT h.*, b.building_name, b.building_number " +
            "FROM house h LEFT JOIN building b ON h.building_id = b.id " +
            "WHERE h.building_id = #{buildingId} " +
            "ORDER BY h.floor DESC, h.house_number")
    IPage<House> selectByBuildingId(Page<House> page, @Param("buildingId") Long buildingId);

    @Select("SELECT h.*, b.building_name, b.building_number " +
            "FROM house h LEFT JOIN building b ON h.building_id = b.id " +
            "ORDER BY b.building_number, h.floor DESC, h.house_number")
    IPage<House> selectAllWithBuilding(Page<House> page);

    @Select("SELECT h.*, b.building_name, b.building_number " +
            "FROM house h LEFT JOIN building b ON h.building_id = b.id " +
            "WHERE h.house_number LIKE CONCAT('%', #{houseNumber}, '%') " +
            "OR h.owner_name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR h.resident_name LIKE CONCAT('%', #{keyword}, '%') " +
            "ORDER BY b.building_number, h.floor DESC, h.house_number")
    IPage<House> searchHouses(Page<House> page, @Param("keyword") String keyword,
                              @Param("houseNumber") String houseNumber);

    @Select("SELECT * FROM house WHERE house_number = #{houseNumber}")
    House selectByHouseNumber(@Param("houseNumber") String houseNumber);
}