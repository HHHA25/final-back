package com.property.system.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class ComplaintSubmitDTO {
    @NotBlank(message = "房号不能为空")
    private String houseNumber;

    @NotBlank(message = "住户姓名不能为空")
    private String residentName;

    @NotBlank(message = "联系电话不能为空")
    private String phone;

    @NotBlank(message = "投诉类型不能为空")
    private String type;

    @NotBlank(message = "投诉内容不能为空")
    private String content;
}
