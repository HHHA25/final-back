package com.property.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.property.system.common.Result;
import com.property.system.dto.FeeAddDTO;
import com.property.system.dto.FeePayDTO;
import com.property.system.entity.Fee;

/**
 * 物业费服务接口
 */
public interface FeeService extends IService<Fee> {
    // 添加物业费（管理员）
    Result<Void> add(FeeAddDTO dto);

    // 查询用户的物业费（通过房号分页）
    Result<IPage<Fee>> getUserFees(String houseNumber, Integer pageNum, Integer pageSize);

    // 查询所有物业费（管理员，分页）
    Result<IPage<Fee>> getAllFees(Integer pageNum, Integer pageSize);

    // 缴纳物业费（通过房号）
    Result<Void> pay(FeePayDTO dto, String houseNumber);

    // 添加搜索方法实现
    Result<IPage<Fee>> searchFees(String keyword, String houseNumber, Integer pageNum, Integer pageSize);
}