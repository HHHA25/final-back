package com.property.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("fee")
public class Fee {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String houseNumber;    // 房号
    private String residentName;   // 住户姓名
    private BigDecimal amount;     // 金额
    private String month;          // 月份 (格式: yyyy-MM)
    private String status;         // 状态 (已缴/未缴)
    private LocalDateTime paymentDate; // 缴费日期 (格式: yyyy-MM-dd)
}