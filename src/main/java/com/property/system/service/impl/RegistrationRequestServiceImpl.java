package com.property.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.system.common.Result;
import com.property.system.common.exception.BusinessException;
import com.property.system.dto.UserRegisterDTO;
import com.property.system.entity.RegistrationRequest;
import com.property.system.entity.User;
import com.property.system.mapper.RegistrationRequestMapper;
import com.property.system.mapper.UserMapper;
import com.property.system.service.RegistrationRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistrationRequestServiceImpl extends ServiceImpl<RegistrationRequestMapper, RegistrationRequest> implements RegistrationRequestService {

    @Autowired
    private RegistrationRequestMapper registrationRequestMapper;

    @Autowired
    private UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Result<Void> submitRegistration(UserRegisterDTO registerDTO) {
        // 检查用户名是否已存在（包括已注册用户和待审批请求）
        User existUser = userMapper.selectByUsername(registerDTO.getUsername());
        if (existUser != null) {
            throw new BusinessException("用户名已存在");
        }

        RegistrationRequest existRequest = lambdaQuery()
                .eq(RegistrationRequest::getUsername, registerDTO.getUsername())
                .eq(RegistrationRequest::getStatus, "PENDING")
                .one();
        if (existRequest != null) {
            throw new BusinessException("该用户名已有待审批的注册请求");
        }

        RegistrationRequest request = new RegistrationRequest();
        request.setUsername(registerDTO.getUsername());
        request.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        request.setHouseNumber(registerDTO.getHouseNumber());
        request.setStatus("PENDING");
        request.setSubmitTime(LocalDateTime.now());

        registrationRequestMapper.insert(request);
        return Result.success();
    }

    @Override
    public Result<List<RegistrationRequest>> getPendingRequests() {
        List<RegistrationRequest> requests = registrationRequestMapper.selectPendingRequests();
        return Result.success(requests);
    }

    @Override
    public Result<Void> approveRegistration(Long requestId) {
        RegistrationRequest request = registrationRequestMapper.selectById(requestId);
        if (request == null) {
            throw new BusinessException("注册请求不存在");
        }

        if (!"PENDING".equals(request.getStatus())) {
            throw new BusinessException("该注册请求已被处理");
        }

        // 创建用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setName(request.getUsername()); // 默认用户名作为姓名
        user.setHouseNumber(request.getHouseNumber());
        user.setRole("RESIDENT");
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());

        userMapper.insert(user);

        // 更新请求状态
        request.setStatus("APPROVED");
        registrationRequestMapper.updateById(request);

        return Result.success();
    }

    @Override
    public Result<Void> rejectRegistration(Long requestId) {
        RegistrationRequest request = registrationRequestMapper.selectById(requestId);
        if (request == null) {
            throw new BusinessException("注册请求不存在");
        }

        if (!"PENDING".equals(request.getStatus())) {
            throw new BusinessException("该注册请求已被处理");
        }

        request.setStatus("REJECTED");
        registrationRequestMapper.updateById(request);

        return Result.success();
    }

    @Override
    public Integer getPendingCount() {
        return registrationRequestMapper.countPendingRequests();
    }

    @Override
    public Result<List<RegistrationRequest>> getProcessedRequests() {
        List<RegistrationRequest> requests = lambdaQuery()
                .in(RegistrationRequest::getStatus, "APPROVED", "REJECTED")
                .orderByDesc(RegistrationRequest::getSubmitTime)
                .list();
        return Result.success(requests);
    }
}