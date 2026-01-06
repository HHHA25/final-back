package com.property.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("registration_request")
public class RegistrationRequest {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String houseNumber;
    private String status; // PENDING/APPROVED/REJECTED
    private LocalDateTime submitTime;
}