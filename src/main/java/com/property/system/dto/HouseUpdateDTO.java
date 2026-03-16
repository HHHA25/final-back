package com.property.system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class HouseUpdateDTO {
    @NotNull(message = "房屋ID不能为空")
    private Long id;

    @NotNull(message = "楼栋ID不能为空")
    private Long buildingId;

    @NotBlank(message = "房号不能为空")
    private String houseNumber;

    @NotNull(message = "楼层不能为空")
    private Integer floor;

    private String unitType;
    private BigDecimal area;
    private String houseStatus;

    // 业主信息
    private String ownerName;
    private String ownerPhone;
    private String ownerIdCard;

    // 住户信息（简化）
    private String residentName;
    private String residentPhone;
}