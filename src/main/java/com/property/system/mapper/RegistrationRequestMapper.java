package com.property.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.property.system.entity.RegistrationRequest;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RegistrationRequestMapper extends BaseMapper<RegistrationRequest> {
    @Select("SELECT * FROM registration_request WHERE status = 'PENDING' ORDER BY submit_time DESC")
    List<RegistrationRequest> selectPendingRequests();

    @Select("SELECT COUNT(*) FROM registration_request WHERE status = 'PENDING'")
    Integer countPendingRequests();

    @Select("SELECT * FROM registration_request WHERE status IN ('APPROVED', 'REJECTED') ORDER BY submit_time DESC")
    List<RegistrationRequest> selectProcessedRequests();

}