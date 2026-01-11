package com.property.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.system.entity.Fee;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 物业费Mapper
 */
public interface FeeMapper extends BaseMapper<Fee> {

    // 查询用户的物业费（分页）
    @Select("SELECT * FROM fee WHERE house_number = #{houseNumber} ORDER BY month DESC")
    IPage<Fee> selectByHouseNumber(Page<Fee> page, @Param("houseNumber") String houseNumber);

    // 查询所有物业费（管理员，分页）
    @Select("SELECT * FROM fee ORDER BY month DESC")
    IPage<Fee> selectAll(Page<Fee> page);
}