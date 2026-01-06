package com.property.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.system.entity.Complaint;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ComplaintMapper extends BaseMapper<Complaint> {
    @Select("SELECT * FROM complaint WHERE house_number = #{houseNumber} ORDER BY submit_time DESC")
    IPage<Complaint> selectByHouseNumber(Page<Complaint> page, @Param("houseNumber") String houseNumber);

    @Select("SELECT * FROM complaint ORDER BY submit_time DESC")
    IPage<Complaint> selectAll(Page<Complaint> page);
}