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
    @Select("SELECT * FROM fee WHERE user_id = #{userId} ORDER BY deadline DESC")
    IPage<Fee> selectByUserId(Page<Fee> page, @Param("userId") Long userId);

    // 查询所有物业费（管理员，分页）
    @Select("SELECT f.*, u.name as userName, u.house_number as houseNumber " +
            "FROM fee f LEFT JOIN user u ON f.user_id = u.id " +
            "ORDER BY f.deadline DESC")
    IPage<Fee> selectAll(Page<Fee> page);
}