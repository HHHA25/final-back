package com.property.system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ParkingUpdateDTO {
    @NotBlank(message = "车位ID不能为空")
    private Long parkingId;

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
