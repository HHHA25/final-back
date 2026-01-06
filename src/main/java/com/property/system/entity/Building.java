package com.property.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("building")
public class Building {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String buildingNumber;  // 楼栋编号
    private String buildingName;    // 楼栋名称
    private Integer totalFloors;    // 总层数
    private Integer totalUnits;     // 总户数
    private String buildingType;    // 楼栋类型
    private LocalDateTime completionDate; // 建成日期
    private String status;          // 状态
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}