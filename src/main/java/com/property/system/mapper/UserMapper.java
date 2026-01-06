// 路径：src/main/java/com/property/system/mapper/UserMapper.java
package com.property.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.property.system.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户Mapper：数据库操作
 */
public interface UserMapper extends BaseMapper<User> {
    // 根据用户名查询用户（登录用）
    @Select("select * from user where username = #{username}")
    User selectByUsername(String username);
}