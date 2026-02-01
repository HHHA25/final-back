package com.property.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.system.common.Result;
import com.property.system.common.exception.BusinessException;
import com.property.system.dto.ComplaintSubmitDTO;
import com.property.system.dto.ComplaintUpdateDTO;
import com.property.system.entity.Complaint;
import com.property.system.mapper.ComplaintMapper;
import com.property.system.service.ComplaintService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ComplaintServiceImpl extends ServiceImpl<ComplaintMapper, Complaint> implements ComplaintService {

    @Override
    public Result<Void> submit(ComplaintSubmitDTO dto) {
        Complaint complaint = new Complaint();
        complaint.setHouseNumber(dto.getHouseNumber());
        complaint.setResidentName(dto.getResidentName());
        complaint.setPhone(dto.getPhone());
        complaint.setType(dto.getType());
        complaint.setContent(dto.getContent());
        complaint.setStatus("PENDING");
        complaint.setSubmitTime(LocalDateTime.now());

        baseMapper.insert(complaint);
        return Result.success();
    }

    @Override
    public Result<IPage<Complaint>> getUserComplaints(String houseNumber, Integer pageNum, Integer pageSize) {
        Page<Complaint> page = new Page<>(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);
        IPage<Complaint> complaintPage = baseMapper.selectByHouseNumber(page, houseNumber);
        return Result.success(complaintPage);
    }

    @Override
    public Result<IPage<Complaint>> getAllComplaints(Integer pageNum, Integer pageSize) {
        Page<Complaint> page = new Page<>(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);
        IPage<Complaint> complaintPage = baseMapper.selectAll(page);
        return Result.success(complaintPage);
    }

    @Override
    public Result<Void> updateStatus(ComplaintUpdateDTO dto) {
        Complaint complaint = baseMapper.selectById(dto.getComplaintId());
        if (complaint == null) {
            throw new BusinessException("投诉记录不存在");
        }

        if ("RESOLVED".equals(dto.getStatus()) && (dto.getHandleResult() == null || dto.getHandleResult().isEmpty())) {
            throw new BusinessException("处理完成时必须填写处理结果");
        }

        complaint.setStatus(dto.getStatus());
        complaint.setHandleResult(dto.getHandleResult());
        baseMapper.updateById(complaint);

        return Result.success();
    }

    @Override
    public Result<IPage<Complaint>> searchComplaints(String keyword, String houseNumber, Integer pageNum, Integer pageSize) {
        Page<Complaint> page = new Page<>(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);

        LambdaQueryWrapper<Complaint> queryWrapper = new LambdaQueryWrapper<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            queryWrapper.and(wrapper ->
                    wrapper.like(Complaint::getHouseNumber, keyword)
                            .or()
                            .like(Complaint::getResidentName, keyword));
        }

        if (houseNumber != null && !houseNumber.trim().isEmpty()) {
            queryWrapper.like(Complaint::getHouseNumber, houseNumber);
        }

        queryWrapper.orderByDesc(Complaint::getSubmitTime);

        IPage<Complaint> complaintPage = baseMapper.selectPage(page, queryWrapper);
        return Result.success(complaintPage);
    }
}