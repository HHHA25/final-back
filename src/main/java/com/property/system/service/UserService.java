// 路径：src/main/java/com/property/system/service/UserService.java
package com.property.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.property.system.common.Result;
import com.property.system.dto.*;
import com.property.system.entity.User;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {
    // 登录
    Result<String> login(UserLoginDTO loginDTO);

    // 注册（居民）
    Result<Void> register(UserRegisterDTO registerDTO);

    // 根据用户名查询用户
    User getByUsername(String username);
    // 管理员直接创建用户
    Result<Void> createUser(UserCreateDTO dto);

    Result<Void> changePassword(String username, ChangePasswordDTO dto);
    Result<Void> forgetPassword(ForgetPasswordDTO dto);
}