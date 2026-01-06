// 路径：src/main/java/com/property/system/entity/User.java
package com.property.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类（对应数据库user表）
 */
@Data
@TableName("user")  // 绑定数据库表名
public class User {
    @TableId(type = IdType.AUTO)  // 主键自增
    private Long id;
    private String username;  // 用户名（登录用）
    private String password;  // 加密后的密码
    private String name;      // 姓名
    private String houseNumber;  // 房号（居民）
    private String phone;     // 手机号
    private String role;      // 角色：ADMIN/RESIDENT
    private Integer status;   // 状态：1-正常，0-禁用
    private LocalDateTime createTime;  // 创建时间
}