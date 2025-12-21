package com.example.baoyan_assistant.controller;

import com.example.baoyan_assistant.dto.LoginRequestDTO;
import com.example.baoyan_assistant.entity.User;
import com.example.baoyan_assistant.repository.UserRepository;
import com.example.baoyan_assistant.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 认证控制器
 * 提供用户登录的 RESTful API 接口
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户登录
     * POST /api/auth/login
     * @param loginRequest 登录请求，包含用户名和密码
     * @return 登录成功返回用户信息和JWT令牌，失败返回401
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        // 根据用户名查询用户
        Optional<User> optionalUser = userRepository.findByUsername(loginRequest.getUsername());

        if (optionalUser.isEmpty()) {
            // 用户不存在
            Map<String, String> error = new HashMap<>();
            error.put("error", "用户名或密码错误");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }

        User user = optionalUser.get();

        // 校验密码
        if (!org.mindrot.jbcrypt.BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
            // 密码错误
            Map<String, String> error = new HashMap<>();
            error.put("error", "用户名或密码错误");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }

        // 校验用户状态
        if (!"active".equals(user.getStatus())) {
            // 用户状态不可用
            Map<String, String> error = new HashMap<>();
            error.put("error", "用户账号已禁用");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }

        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);

        // 生成JWT令牌
        String token = jwtUtil.generateToken(user.getId(), user.getRole());

        // 构建返回结果
        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId());
        response.put("role", user.getRole());
        response.put("realName", user.getRealName());
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
}
