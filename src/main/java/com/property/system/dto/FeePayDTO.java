package com.property.system.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

/**
 * 缴纳物业费DTO
 */
@Data
public class FeePayDTO {
    @NotNull(message = "物业费ID不能为空")
    private Long feeId;
}