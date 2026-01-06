package com.property.system.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 更新维修状态DTO（管理员用）
 */
@Data
public class RepairUpdateDTO {
    @NotNull(message = "维修ID不能为空")
    private Long repairId;

    @NotBlank(message = "状态不能为空")
    private String status;  // PENDING/PROCESSING/COMPLETED

    private String feedback;  // 处理反馈
}