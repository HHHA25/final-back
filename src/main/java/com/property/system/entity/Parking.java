package com.property.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("parking")
public class Parking {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String parkingNumber;
    private String houseNumber;
    private String residentName;
    private String carPlate;
    private String status; // ASSIGNED/FREE
    private LocalDateTime startTime;
}