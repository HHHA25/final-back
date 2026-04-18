package com.property.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.system.common.Result;
import com.property.system.common.exception.BusinessException;
import com.property.system.dto.*;
import com.property.system.entity.Fee;
import com.property.system.entity.House;
import com.property.system.mapper.FeeMapper;
import com.property.system.mapper.HouseMapper;
import com.property.system.mapper.UserMapper;
import com.property.system.service.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FeeServiceImpl extends ServiceImpl<FeeMapper, Fee> implements FeeService {

    @Autowired
    private FeeMapper feeMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HouseMapper houseMapper;  // 用于校验房号是否存在

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
    // 添加搜索方法实现
    @Override
    public Result<IPage<Fee>> searchFees(String keyword, String houseNumber, Integer pageNum, Integer pageSize) {
        Page<Fee> page = new Page<>(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);

        LambdaQueryWrapper<Fee> queryWrapper = new LambdaQueryWrapper<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            queryWrapper.and(wrapper ->
                    wrapper.like(Fee::getHouseNumber, keyword)
                            .or()
                            .like(Fee::getResidentName, keyword));
        }



        if (houseNumber != null && !houseNumber.trim().isEmpty()) {
            queryWrapper.like(Fee::getHouseNumber, houseNumber);
        }

        queryWrapper.orderByDesc(Fee::getMonth);

        IPage<Fee> feePage = baseMapper.selectPage(page, queryWrapper);
        return Result.success(feePage);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> batchAdd(BatchFeeAddDTO dto) {
        String month = dto.getMonth();
        List<String> houseNumbers = dto.getHouseNumbers();

        // 1. 校验月份格式（假设 yyyy-MM）
        if (!month.matches("\\d{4}-\\d{2}")) {
            throw new BusinessException("月份格式不正确，应为 yyyy-MM");
        }

        // 2. 校验所有房号是否存在
        for (String houseNumber : houseNumbers) {
            House house = houseMapper.selectByHouseNumber(houseNumber);
            if (house == null) {
                throw new BusinessException("房号 " + houseNumber + " 不存在");
            }
        }

        // 3. 批量添加
        List<Fee> feeList = new ArrayList<>();
        for (String houseNumber : houseNumbers) {
            // 查询上个月该房号的物业费金额
            BigDecimal amount = getPreviousMonthAmount(houseNumber, month);
            // 获取住户姓名（从房屋表或用户表）
            String residentName = getResidentName(houseNumber);

            Fee fee = new Fee();
            fee.setHouseNumber(houseNumber);
            fee.setResidentName(residentName);
            fee.setAmount(amount);
            fee.setMonth(month);
            fee.setStatus("UNPAID");
            fee.setPaymentDate(null);
            feeList.add(fee);
        }

        // 批量插入
        saveBatch(feeList);
        return Result.success();
    }

    /**
     * 获取指定房号上个月的物业费金额
     * 如果没有上个月记录，则返回默认金额 0
     */
    private BigDecimal getPreviousMonthAmount(String houseNumber, String currentMonth) {
        // 解析当前月份，计算上个月
        String[] parts = currentMonth.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        if (month == 1) {
            year--;
            month = 12;
        } else {
            month--;
        }
        String previousMonth = String.format("%04d-%02d", year, month);

        // 查询上个月记录
        LambdaQueryWrapper<Fee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Fee::getHouseNumber, houseNumber)
                .eq(Fee::getMonth, previousMonth);
        Fee previousFee = getOne(wrapper);
        if (previousFee != null) {
            return previousFee.getAmount();
        }
        // 如果没有上个月记录，可以返回默认值（比如从房屋固定物业费字段获取，或返回0）
        // 这里简单返回 0，实际可根据业务调整
        return BigDecimal.ZERO;
    }

    @Override
    public Result<Void> delete(Long feeId) {
        Fee fee = baseMapper.selectById(feeId);
        if (fee == null) {
            throw new BusinessException("物业费记录不存在");
        }
        baseMapper.deleteById(feeId);
        return Result.success();
    }

    @Override
    public Result<Void> update(FeeUpdateDTO dto) {
        // 1. 检查记录是否存在
        Fee fee = baseMapper.selectById(dto.getId());
        if (fee == null) {
            throw new BusinessException("物业费记录不存在");
        }

        // 2. 检查房号+月份是否已被其他记录占用（可选，根据业务决定）
        LambdaQueryWrapper<Fee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Fee::getHouseNumber, dto.getHouseNumber())
                .eq(Fee::getMonth, dto.getMonth())
                .ne(Fee::getId, dto.getId());
        Long count = baseMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException("该房号在 " + dto.getMonth() + " 月份已有物业费记录");
        }

        // 3. 更新字段
        fee.setHouseNumber(dto.getHouseNumber());
        fee.setResidentName(dto.getResidentName());
        fee.setAmount(dto.getAmount());
        fee.setMonth(dto.getMonth());
        // 注意：status 和 paymentDate 不应通过编辑接口修改，保持原值

        // 4. 保存
        baseMapper.updateById(fee);
        return Result.success();
    }

    /**
     * 获取住户姓名：优先从房屋表获取 residentName，如果没有则返回房号
     */
    private String getResidentName(String houseNumber) {
        House house = houseMapper.selectByHouseNumber(houseNumber);
        if (house != null && house.getResidentName() != null && !house.getResidentName().isEmpty()) {
            return house.getResidentName();
        }
        // 如果没有住户姓名，可以返回房号或默认值
        return houseNumber;
    }

}