package com.example.baoyan_assistant.service;

import com.example.baoyan_assistant.entity.User;
import com.example.baoyan_assistant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * 管理员用户管理服务
 * 用于管理员创建和管理系统用户（学生、审核老师、管理员）
 * 注意：这是内部服务，不是对外接口
 */
@Service
@Transactional
public class AdminUserManagementService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 生成随机密码
     * @return 随机密码字符串
     */
    private String generateRandomPassword() {
        // 使用UUID生成随机字符串，截取前8位作为密码
        String randomStr = UUID.randomUUID().toString().replaceAll("-", "");
        return randomStr.substring(0, 8);
    }

    /**
     * 创建学生用户
     * @param studentId 学号
     * @param realName 真实姓名
     * @return 创建的用户对象
     * @throws RuntimeException 如果用户名已存在
     */
    public User createStudentUser(String studentId, String realName) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(studentId)) {
            throw new RuntimeException("用户名已存在: " + studentId);
        }

        // 生成随机密码并加密
        String randomPassword = generateRandomPassword();
        String encryptedPassword = BCrypt.hashpw(randomPassword, BCrypt.gensalt());

        User user = new User();
        user.setUsername(studentId);
        user.setPassword(encryptedPassword);
        user.setRole("student");
        user.setRealName(realName);
        user.setStudentId(studentId);
        user.setStatus("active");

        return userRepository.save(user);
    }

    /**
     * 创建审核老师用户
     * @param teacherId 工号
     * @param realName 真实姓名
     * @return 创建的用户对象
     * @throws RuntimeException 如果用户名已存在
     */
    public User createReviewerUser(String teacherId, String realName) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(teacherId)) {
            throw new RuntimeException("用户名已存在: " + teacherId);
        }

        // 生成随机密码并加密
        String randomPassword = generateRandomPassword();
        String encryptedPassword = BCrypt.hashpw(randomPassword, BCrypt.gensalt());

        User user = new User();
        user.setUsername(teacherId);
        user.setPassword(encryptedPassword);
        user.setRole("reviewer");
        user.setRealName(realName);
        user.setTeacherId(teacherId);
        user.setStatus("active");

        return userRepository.save(user);
    }

    /**
     * 创建管理员用户
     * @param username 用户名
     * @param realName 真实姓名
     * @return 创建的用户对象
     * @throws RuntimeException 如果用户名已存在
     */
    public User createAdminUser(String username, String realName) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("用户名已存在: " + username);
        }

        // 生成随机密码并加密
        String randomPassword = generateRandomPassword();
        String encryptedPassword = BCrypt.hashpw(randomPassword, BCrypt.gensalt());

        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptedPassword);
        user.setRole("admin");
        user.setRealName(realName);
        user.setStatus("active");

        return userRepository.save(user);
    }
}
