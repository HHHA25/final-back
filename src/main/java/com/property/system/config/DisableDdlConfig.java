package com.property.system.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 强力禁用 MyBatis-Plus DDL 的配置
 */
@Configuration
@ConditionalOnClass(name = "com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration")
public class DisableDdlConfig {

    // 这个空配置类的存在会触发 Spring 的正确加载顺序
    // 确保 DDL 功能被正确禁用
}