package com.property.system.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 提交维修申请DTO
 */
@Data
public class RepairSubmitDTO {
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotBlank(message = "房号不能为空")
    private String houseNumber;

    @NotBlank(message = "住户姓名不能为空")
    private String residentName;

    @NotBlank(message = "联系电话不能为空")
    private String phone;

    @NotBlank(message = "维修类型不能为空")
    private String type;

    private String description;  // 描述可为空

    private String imageUrl;     // 图片路径可为空
}