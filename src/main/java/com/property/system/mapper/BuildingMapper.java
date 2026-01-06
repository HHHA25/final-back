package com.property.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.system.entity.Building;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface BuildingMapper extends BaseMapper<Building> {

    @Select("SELECT * FROM building ORDER BY building_number")
    IPage<Building> selectAll(Page<Building> page);

    @Select("SELECT COUNT(*) FROM house WHERE building_id = #{buildingId}")
    Integer countHousesByBuilding(@Param("buildingId") Long buildingId);

    @Select("SELECT COUNT(*) FROM house WHERE building_id = #{buildingId} AND house_status = 'OCCUPIED'")
    Integer countOccupiedHouses(@Param("buildingId") Long buildingId);
}