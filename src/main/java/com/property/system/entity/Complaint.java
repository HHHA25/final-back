package com.property.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("complaint")
public class Complaint {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String houseNumber;
    private String residentName;
    private String phone;
    private String type;
    private String content;
    private String status; // PENDING/PROCESSING/RESOLVED
    private LocalDateTime submitTime;
    private String handleResult;
}