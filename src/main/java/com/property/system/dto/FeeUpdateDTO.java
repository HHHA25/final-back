package com.property.system.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FeeUpdateDTO {

    @NotNull(message = "物业费ID不能为空")
    private Long id;

    @NotBlank(message = "房号不能为空")
    private String houseNumber;

    @NotBlank(message = "住户姓名不能为空")
    private String residentName;

    @NotNull(message = "金额不能为空")
    @DecimalMin(value = "0.01", message = "金额必须大于0")
    private BigDecimal amount;

    @NotBlank(message = "月份不能为空")
    private String month; // 格式：yyyy-MM
}