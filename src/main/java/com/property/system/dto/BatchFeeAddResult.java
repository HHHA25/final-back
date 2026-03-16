package com.property.system.dto;

import lombok.Data;
import java.util.List;

@Data
public class BatchFeeAddResult {
    private int successCount;               // 成功生成的数量
    private List<String> failedHouseNumbers; // 失败的房号列表
}