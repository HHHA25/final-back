package com.property.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 维修实体类
 */
@Data
@TableName("repair")
public class Repair {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;        // 用户ID（新增）
    private String houseNumber; // 房号
    private String residentName; // 住户姓名
    private String phone;       // 联系电话
    private String type;        // 维修类型
    private String description; // 问题描述
    private String status;      // 状态：PENDING/PROCESSING/COMPLETED
    private LocalDateTime submitTime;   // 提交时间
    private LocalDateTime handleTime; // 完成时间
    private Long handlerId;     // 处理人ID（管理员）
    private String feedback;    // 处理反馈（新增）
}