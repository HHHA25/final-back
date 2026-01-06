package com.property.system;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneratePassword {
    public static void main(String[] args) {
        // 明文密码：123456（必须是明文！）
        String rawPassword = "123456";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rawPassword); // 只加密一次！
        System.out.println("正确的加密串：" + encodedPassword);
    }
}