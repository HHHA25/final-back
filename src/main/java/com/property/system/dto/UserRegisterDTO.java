// 路径：src/main/java/com/property/system/dto/UserRegisterDTO.java
package com.property.system.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * 注册请求DTO
 */
@Data
public class UserRegisterDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "姓名不能为空")
    private String name;

    @NotBlank(message = "房号不能为空")
    private String houseNumber;

    private String phone;  // 手机号可为空
}