package com.property.system.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class BuildingAddDTO {
    @NotBlank(message = "楼栋编号不能为空")
    private String buildingNumber;

    @NotBlank(message = "楼栋名称不能为空")
    private String buildingName;

    @NotNull(message = "总层数不能为空")
    private Integer totalFloors;

    @NotNull(message = "总户数不能为空")
    private Integer totalUnits;

    private String buildingType;
    private String completionDate;
}