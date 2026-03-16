package com.property.system.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class BatchFeeAddDTO {
    @NotBlank(message = "月份不能为空")
    private String month;  // 格式：yyyy-MM

    @NotEmpty(message = "请至少选择一个房号")
    private List<String> houseNumbers;  // 选中的房号列表
}