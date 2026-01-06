package com.property.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.system.common.Result;
import com.property.system.common.exception.BusinessException;
import com.property.system.dto.FeeAddDTO;
import com.property.system.dto.FeePayDTO;
import com.property.system.entity.Fee;
import com.property.system.entity.User;
import com.property.system.mapper.FeeMapper;
import com.property.system.mapper.UserMapper;
import com.property.system.service.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FeeServiceImpl extends ServiceImpl<FeeMapper, Fee> implements FeeService {

    @Autowired
    private FeeMapper feeMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 添加物业费（管理员）
     */
    @Override
    public Result<Void> add(FeeAddDTO dto) {
        // 检查房号是否已存在物业费记录（同一周期）
        Fee existFee = lambdaQuery()
                .eq(Fee::getHouseNumber, dto.getHouseNumber())
                .eq(Fee::getMonth, dto.getMonth())
                .one();
        if (existFee != null) {
            throw new BusinessException("该房号的" + dto.getMonth() + "物业费已添加");
        }

        // 构建物业费
        Fee fee = new Fee();
        fee.setHouseNumber(dto.getHouseNumber());
        fee.setResidentName(dto.getResidentName());
        fee.setAmount(dto.getAmount());
        fee.setMonth(dto.getMonth());
        fee.setStatus("UNPAID");  // 初始未缴

        // 保存
        feeMapper.insert(fee);
        return Result.success();
    }

    /**
     * 查询用户的物业费
     */
    @Override
    public Result<IPage<Fee>> getUserFees(String houseNumber, Integer pageNum, Integer pageSize) {
        // 分页查询
        Page<Fee> page = new Page<>(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);
        IPage<Fee> feePage = lambdaQuery()
                .eq(Fee::getHouseNumber, houseNumber)
                .orderByDesc(Fee::getMonth)
                .page(page);
        return Result.success(feePage);
    }

    /**
     * 查询所有物业费（管理员）
     */
    @Override
    public Result<IPage<Fee>> getAllFees(Integer pageNum, Integer pageSize) {
        Page<Fee> page = new Page<>(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);
        IPage<Fee> feePage = lambdaQuery()
                .orderByDesc(Fee::getMonth)
                .page(page);
        return Result.success(feePage);
    }

    /**
     * 缴纳物业费
     */
    @Override
    public Result<Void> pay(FeePayDTO dto, String houseNumber) {
        // 校验物业费存在
        Fee fee = feeMapper.selectById(dto.getFeeId());
        if (fee == null) {
            throw new BusinessException("物业费记录不存在");
        }

        // 校验是否是自己的物业费
        if (!fee.getHouseNumber().equals(houseNumber)) {
            throw new BusinessException("不能缴纳他人的物业费");
        }

        // 校验是否已缴纳
        if ("PAID".equals(fee.getStatus())) {
            throw new BusinessException("该物业费已缴纳");
        }

        // 更新状态和支付时间
        fee.setStatus("PAID");
        fee.setPaymentDate(LocalDateTime.now());
        feeMapper.updateById(fee);

        return Result.success();
    }
}