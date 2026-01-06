package com.property.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.system.common.Result;
import com.property.system.common.exception.BusinessException;
import com.property.system.dto.RepairSubmitDTO;
import com.property.system.dto.RepairUpdateDTO;
import com.property.system.entity.Repair;
import com.property.system.entity.User;
import com.property.system.mapper.RepairMapper;
import com.property.system.mapper.UserMapper;
import com.property.system.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RepairServiceImpl extends ServiceImpl<RepairMapper, Repair> implements RepairService {

    @Autowired
    private RepairMapper repairMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<Void> submit(RepairSubmitDTO dto) {
        // 校验用户是否存在
        User user = userMapper.selectById(dto.getUserId());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 构建维修申请
        Repair repair = new Repair();
        repair.setUserId(dto.getUserId());
        repair.setHouseNumber(dto.getHouseNumber());
        repair.setResidentName(dto.getResidentName());
        repair.setPhone(dto.getPhone());
        repair.setType(dto.getType());
        repair.setDescription(dto.getDescription());
        repair.setStatus("PENDING");  // 初始状态：待处理
        repair.setSubmitTime(LocalDateTime.now());

        // 保存
        repairMapper.insert(repair);
        return Result.success();
    }

    @Override
    public Result<IPage<Repair>> getUserRepairs(Long userId, Integer pageNum, Integer pageSize) {
        // 校验用户存在
        if (userMapper.selectById(userId) == null) {
            throw new BusinessException("用户不存在");
        }

        // 分页查询
        Page<Repair> page = new Page<>(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);
        IPage<Repair> repairPage = repairMapper.selectByUserId(page, userId);
        return Result.success(repairPage);
    }

    @Override
    public Result<IPage<Repair>> getAllRepairs(Integer pageNum, Integer pageSize) {
        Page<Repair> page = new Page<>(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);
        IPage<Repair> repairPage = repairMapper.selectAll(page);
        return Result.success(repairPage);
    }

    @Override
    public Result<Void> updateStatus(RepairUpdateDTO dto, Long handlerId) {
        // 校验维修申请存在
        Repair repair = repairMapper.selectById(dto.getRepairId());
        if (repair == null) {
            throw new BusinessException("维修申请不存在");
        }

        // 校验状态合法性
        if (!"PROCESSING".equals(dto.getStatus()) && !"COMPLETED".equals(dto.getStatus())) {
            throw new BusinessException("状态只能是PROCESSING或COMPLETED");
        }

        // 完成时必须有反馈
        if ("COMPLETED".equals(dto.getStatus()) && (dto.getFeedback() == null || dto.getFeedback().isEmpty())) {
            throw new BusinessException("维修完成时必须填写反馈");
        }

        // 更新状态
        repair.setStatus(dto.getStatus());
        repair.setFeedback(dto.getFeedback());
        repair.setHandlerId(handlerId);
        repair.setHandleTime(LocalDateTime.now());

        // 如果状态是完成，设置完成时间
        if ("COMPLETED".equals(dto.getStatus())) {
            repair.setHandleTime(LocalDateTime.now());
        }

        repairMapper.updateById(repair);

        return Result.success();
    }
}