package com.property.system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BuildingUpdateDTO {
    @NotNull(message = "楼栋ID不能为空")
    private Long id;

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
    private String status;
}
