// 路径：src/main/java/com/property/system/common/Result.java
package com.property.system.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一返回结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;  // 状态码：200成功，400参数错，401未登录，403无权限，500服务器错
    private String msg;    // 提示信息
    private T data;        // 数据

    // 成功（无数据）
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    // 成功（有数据）
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    // 失败
    public static <T> Result<T> fail(Integer code, String msg) {
        return new Result<>(code, msg, null);
    }

    // 未登录
    public static <T> Result<T> unAuth() {
        return new Result<>(401, "请先登录", null);
    }

    // 无权限
    public static <T> Result<T> forbidden() {
        return new Result<>(403, "无操作权限", null);
    }
}