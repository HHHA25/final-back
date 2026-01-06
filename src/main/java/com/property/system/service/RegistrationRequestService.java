package com.property.system.service;

import com.property.system.common.Result;
import com.property.system.dto.UserRegisterDTO;
import com.property.system.entity.RegistrationRequest;

import java.util.List;

public interface RegistrationRequestService {
    Result<Void> submitRegistration(UserRegisterDTO registerDTO);
    Result<List<RegistrationRequest>> getPendingRequests();
    Result<Void> approveRegistration(Long requestId);
    Result<Void> rejectRegistration(Long requestId);
    Integer getPendingCount();
}