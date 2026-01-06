// 路径：src/main/java/com/property/system/common/exception/BusinessException.java
package com.property.system.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义业务异常
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessException extends RuntimeException {
    private Integer code;  // 异常码
    private String message;  // 异常信息

    public BusinessException(String message) {
        this(400, message);
    }
}