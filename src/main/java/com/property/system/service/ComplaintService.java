package com.property.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.property.system.common.Result;
import com.property.system.dto.ComplaintSubmitDTO;
import com.property.system.dto.ComplaintUpdateDTO;
import com.property.system.entity.Complaint;

public interface ComplaintService {
    Result<Void> submit(ComplaintSubmitDTO dto);
    Result<IPage<Complaint>> getUserComplaints(String houseNumber, Integer pageNum, Integer pageSize);
    Result<IPage<Complaint>> getAllComplaints(Integer pageNum, Integer pageSize);
    Result<Void> updateStatus(ComplaintUpdateDTO dto);
}