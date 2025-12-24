package com.example.baoyan_assistant.util;

/**
 * 用户上下文工具类
 * 基于ThreadLocal存储当前请求的用户身份信息
 */
public class UserContext {

    private static final ThreadLocal<Long> userIdThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<String> userRoleThreadLocal = new ThreadLocal<>();

    /**
     * 设置用户ID
     * @param userId 用户ID
     */
    public static void setUserId(Long userId) {
        userIdThreadLocal.set(userId);
    }

    /**
     * 获取用户ID
     * @return 用户ID
     */
    public static Long getUserId() {
        return userIdThreadLocal.get();
    }

    /**
     * 设置用户角色
     * @param userRole 用户角色
     */
    public static void setUserRole(String userRole) {
        userRoleThreadLocal.set(userRole);
    }

    /**
     * 获取用户角色
     * @return 用户角色
     */
    public static String getUserRole() {
        return userRoleThreadLocal.get();
    }

    /**
     * 清理ThreadLocal，防止线程复用污染
     */
    public static void clear() {
        userIdThreadLocal.remove();
        userRoleThreadLocal.remove();
    }
}