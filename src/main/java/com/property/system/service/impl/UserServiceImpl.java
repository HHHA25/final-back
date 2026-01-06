// 路径：src/main/java/com/property/system/service/impl/UserServiceImpl.java
package com.property.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.system.common.Result;
import com.property.system.common.exception.BusinessException;
import com.property.system.dto.UserLoginDTO;
import com.property.system.dto.UserRegisterDTO;
import com.property.system.entity.User;
import com.property.system.mapper.UserMapper;
import com.property.system.service.UserService;
import com.property.system.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户服务实现
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtils jwtUtils;

    // 密码加密器
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 登录：验证账号密码，返回Token
     */
    @Override
    public Result<String> login(UserLoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        System.out.println("登录请求：username=" + username + ", password=" + password); // 打印请求参数
        // 1. 查询用户
        User user = userMapper.selectByUsername(username);
        System.out.println("查询到的用户：" + (user == null ? "null" : user.toString())); // 打印查询结果
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        // 2. 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        System.out.println("密码验证结果：" + passwordEncoder.matches(password, user.getPassword())); // 打印验证结果
        // 3. 验证状态（是否禁用）
        if (user.getStatus() != 1) {
            throw new BusinessException("账号已禁用，请联系管理员");
        }

        // 4. 生成Token
        String token = jwtUtils.generateToken(user.getUsername(), user.getRole());
        return Result.success(token);
    }

    /**
     * 注册：居民注册账号
     */
    @Override
    public Result<Void> register(UserRegisterDTO registerDTO) {
        String username = registerDTO.getUsername();

        // 1. 校验用户名是否已存在
        User existUser = userMapper.selectByUsername(username);
        if (existUser != null) {
            throw new BusinessException("用户名已存在");
        }

        // 2. 密码加密
        String encodedPassword = passwordEncoder.encode(registerDTO.getPassword());

        // 3. 构建用户对象
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setName(registerDTO.getName());
        user.setHouseNumber(registerDTO.getHouseNumber());
        user.setPhone(registerDTO.getPhone());
        user.setRole("RESIDENT");  // 注册默认为居民
        user.setStatus(1);  // 默认为正常状态
        user.setCreateTime(LocalDateTime.now());

        // 4. 保存到数据库
        userMapper.insert(user);
        return Result.success();
    }

    /**
     * 根据用户名查询用户
     */
    @Override
    public User getByUsername(String username) {
        return userMapper.selectByUsername(username);
    }
}