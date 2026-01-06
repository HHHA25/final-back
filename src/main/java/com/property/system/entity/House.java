package com.property.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("house")
public class House {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long buildingId;        // 楼栋ID
    private String houseNumber;     // 房号
    private Integer floor;          // 楼层
    private String unitType;        // 户型
    private BigDecimal area;        // 面积
    private Integer roomCount;      // 房间数
    private Integer livingRoomCount; // 客厅数
    private Integer bathroomCount;  // 卫生间数
    private String orientation;     // 朝向
    private String houseStatus;     // 房屋状态
    private String ownerName;       // 业主姓名
    private String ownerPhone;      // 业主电话
    private String ownerIdCard;     // 业主身份证
    private String residentName;    // 住户姓名
    private String residentPhone;   // 住户电话
    private String residentType;    // 住户类型
    private LocalDateTime contractStartDate; // 租约开始
    private LocalDateTime contractEndDate;   // 租约结束
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}