package com.property.system.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserInfoDTO {
    private Long id;
    private String username;
    private String name;
    private String houseNumber;
    private String phone;
    private String role;
    private Integer status;
    private LocalDateTime createTime;
}