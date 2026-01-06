package com.property.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.system.entity.Repair;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 维修Mapper
 */
public interface RepairMapper extends BaseMapper<Repair> {
    // 分页查询用户的维修申请
    @Select("SELECT * FROM repair WHERE user_id = #{userId} ORDER BY submit_time DESC")
    IPage<Repair> selectByUserId(Page<Repair> page, @Param("userId") Long userId);

    // 分页查询所有维修申请（管理员）
    @Select("SELECT * FROM repair ORDER BY submit_time DESC")
    IPage<Repair> selectAll(Page<Repair> page);
}