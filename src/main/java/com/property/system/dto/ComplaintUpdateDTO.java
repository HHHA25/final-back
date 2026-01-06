package com.property.system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ComplaintUpdateDTO {
    @NotBlank(message = "投诉ID不能为空")
    private Long complaintId;

    @NotBlank(message = "状态不能为空")
    private String status;

    private String handleResult;
}
