package com.property.system.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class ParkingAddDTO {
    @NotBlank(message = "车位号不能为空")
    private String parkingNumber;

    @NotBlank(message = "房号不能为空")
    private String houseNumber;

    @NotBlank(message = "住户姓名不能为空")
    private String residentName;

    @NotBlank(message = "车牌号不能为空")
    private String carPlate;

    @NotBlank(message = "状态不能为空")
    private String status;
}

