package com.example.baoyan_assistant.dto;

/**
 * 登录请求DTO
 * 用于接收登录请求的用户名和密码
 */
public class LoginRequestDTO {
    private String username;
    private String password;

    // ==================== Getter 和 Setter 方法 ====================

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
