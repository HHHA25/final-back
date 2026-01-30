package com.property.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.property.system.common.Result;
import com.property.system.dto.RepairSubmitDTO;
import com.property.system.dto.RepairUpdateDTO;
import com.property.system.entity.Repair;

public interface RepairService {
    Result<Void> submit(RepairSubmitDTO dto);
    Result<IPage<Repair>> getUserRepairs(Long userId, Integer pageNum, Integer pageSize);
    Result<IPage<Repair>> getAllRepairs(Integer pageNum, Integer pageSize);
    Result<Void> updateStatus(RepairUpdateDTO dto, Long handlerId);

    // 添加搜索方法
    Result<IPage<Repair>> searchRepairs(String keyword, String houseNumber, Integer pageNum, Integer pageSize);
}