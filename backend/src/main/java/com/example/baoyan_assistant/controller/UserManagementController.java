package com.example.baoyan_assistant.controller;

import com.example.baoyan_assistant.entity.User;
import com.example.baoyan_assistant.service.AdminUserManagementService;
import com.example.baoyan_assistant.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 管理员用户管理控制器
 * 提供管理员端的用户管理API
 */
@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin(origins = "*")
public class UserManagementController {

    @Autowired
    private AdminUserManagementService adminUserManagementService;

    /**
     * 检查当前用户是否为管理员
     */
    private void checkAdminRole() {
        String role = UserContext.getUserRole();
        if (!"admin".equals(role)) {
            throw new RuntimeException("权限不足");
        }
    }

    /**
     * 获取用户列表
     * @param role 角色（可选）
     * @param keyword 关键字（可选）
     * @return 用户列表（不含密码字段）
     */
    @GetMapping
    public ResponseEntity<?> getUsers(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String keyword) {
        try {
            checkAdminRole();
            
            // 调用服务层获取用户列表
            List<User> users = adminUserManagementService.listUsers(
                    Optional.ofNullable(role),
                    Optional.ofNullable(keyword)
            );
            
            // 移除密码字段并返回
            List<?> userList = users.stream()
                    .map(user -> {
                        // 创建一个新的对象，不包含密码字段
                        return new Object() {
                            private Long id = user.getId();
                            private String username = user.getUsername();
                            private String realName = user.getRealName();
                            private String role = user.getRole();
                            private String status = user.getStatus();
                            private String studentId = user.getStudentId();
                            private String teacherId = user.getTeacherId();
                            private String email = user.getEmail();
                            private String phone = user.getPhone();
                            private String createTime = user.getCreateTime().toString();
                            private String updateTime = user.getUpdateTime().toString();
                            private String lastLoginTime = user.getLastLoginTime() != null ? user.getLastLoginTime().toString() : null;
                            
                            // Getter方法（用于JSON序列化）
                            public Long getId() { return id; }
                            public String getUsername() { return username; }
                            public String getRealName() { return realName; }
                            public String getRole() { return role; }
                            public String getStatus() { return status; }
                            public String getStudentId() { return studentId; }
                            public String getTeacherId() { return teacherId; }
                            public String getEmail() { return email; }
                            public String getPhone() { return phone; }
                            public String getCreateTime() { return createTime; }
                            public String getUpdateTime() { return updateTime; }
                            public String getLastLoginTime() { return lastLoginTime; }
                        };
                    })
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(userList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("ERROR", e.getMessage()));
        }
    }

    /**
     * 创建学生用户
     * @param request 创建学生请求体
     * @return 创建的用户信息（不含密码）
     */
    @PostMapping("/student")
    public ResponseEntity<?> createStudentUser(@RequestBody CreateStudentRequest request) {
        try {
            checkAdminRole();
            
            User user = adminUserManagementService.createStudentUser(
                    request.getStudentId(),
                    request.getRealName()
            );
            
            // 返回用户信息（不含密码）
            return ResponseEntity.ok(createUserResponse(user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("ERROR", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("ERROR", e.getMessage()));
        }
    }

    /**
     * 创建审核老师用户
     * @param request 创建审核老师请求体
     * @return 创建的用户信息（不含密码）
     */
    @PostMapping("/reviewer")
    public ResponseEntity<?> createReviewerUser(@RequestBody CreateReviewerRequest request) {
        try {
            checkAdminRole();
            
            User user = adminUserManagementService.createReviewerUser(
                    request.getTeacherId(),
                    request.getRealName()
            );
            
            // 返回用户信息（不含密码）
            return ResponseEntity.ok(createUserResponse(user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("ERROR", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("ERROR", e.getMessage()));
        }
    }

    /**
     * 创建管理员用户
     * @param request 创建管理员请求体
     * @return 创建的用户信息（不含密码）
     */
    @PostMapping("/admin")
    public ResponseEntity<?> createAdminUser(@RequestBody CreateAdminRequest request) {
        try {
            checkAdminRole();
            
            User user = adminUserManagementService.createAdminUser(
                    request.getUsername(),
                    request.getRealName()
            );
            
            // 返回用户信息（不含密码）
            return ResponseEntity.ok(createUserResponse(user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("ERROR", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("ERROR", e.getMessage()));
        }
    }
    
    /**
     * 创建用户（通用）
     * @param request 创建用户请求体
     * @return 创建的用户信息（不含密码）
     */
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        try {
            checkAdminRole();
            
            User user;
            if ("student".equals(request.getRole())) {
                // 创建学生用户
                user = adminUserManagementService.createStudentUser(
                        request.getUsername(),
                        ""
                );
            } else if ("reviewer".equals(request.getRole())) {
                // 创建审核老师用户
                user = adminUserManagementService.createReviewerUser(
                        request.getUsername(),
                        ""
                );
            } else if ("admin".equals(request.getRole())) {
                // 创建管理员用户
                user = adminUserManagementService.createAdminUser(
                        request.getUsername(),
                        ""
                );
            } else {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("ERROR", "无效的角色类型"));
            }
            
            // 返回用户信息（不含密码）
            return ResponseEntity.ok(createUserResponse(user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("ERROR", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("ERROR", e.getMessage()));
        }
    }

    /**
     * 重置用户密码
     * @param userId 用户ID
     * @return 重置后的密码信息
     */
    @PostMapping("/reset-password/{userId}")
    public ResponseEntity<?> resetPassword(@PathVariable Long userId) {
        try {
            checkAdminRole();
            
            String newPassword = adminUserManagementService.resetPassword(userId);
            
            // 返回重置后的密码信息
            return ResponseEntity.ok(new ResetPasswordResponse(userId, newPassword));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("ERROR", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("ERROR", e.getMessage()));
        }
    }

    /**
     * 更新用户状态
     * @param userId 用户ID
     * @param request 更新状态请求体
     * @return 更新后的状态信息
     */
    @PatchMapping("/status/{userId}")
    public ResponseEntity<?> updateStatus(@PathVariable Long userId, @RequestBody UpdateStatusRequest request) {
        try {
            checkAdminRole();
            
            // 将Boolean类型转换为String类型（active/disabled）
            String statusStr = request.getStatus() ? "active" : "disabled";
            adminUserManagementService.updateStatus(userId, statusStr);
            
            // 返回更新后的状态信息
            return ResponseEntity.ok(new UpdateStatusResponse(userId, statusStr));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("ERROR", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("ERROR", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("ERROR", e.getMessage()));
        }
    }

    /**
     * 创建用户响应对象（不含密码）
     * @param user 用户实体
     * @return 用户响应对象
     */
    private UserResponse createUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.getRealName(),
                user.getStatus()
        );
    }

    // 请求和响应实体类
    
    /**
     * 创建学生请求体
     */
    static class CreateStudentRequest {
        private String studentId;
        private String realName;
        
        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }
        public String getRealName() { return realName; }
        public void setRealName(String realName) { this.realName = realName; }
    }

    /**
     * 创建审核老师请求体
     */
    static class CreateReviewerRequest {
        private String teacherId;
        private String realName;
        
        public String getTeacherId() { return teacherId; }
        public void setTeacherId(String teacherId) { this.teacherId = teacherId; }
        public String getRealName() { return realName; }
        public void setRealName(String realName) { this.realName = realName; }
    }

    /**
     * 创建管理员请求体
     */
    static class CreateAdminRequest {
        private String username;
        private String realName;
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getRealName() { return realName; }
        public void setRealName(String realName) { this.realName = realName; }
    }
    
    /**
     * 创建用户通用请求体
     */
    static class CreateUserRequest {
        private String username;
        private String role;
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }

    /**
     * 用户响应对象（不含密码）
     */
    static class UserResponse {
        private Long userId;
        private String username;
        private String role;
        private String realName;
        private String status;
        
        public UserResponse(Long userId, String username, String role, String realName, String status) {
            this.userId = userId;
            this.username = username;
            this.role = role;
            this.realName = realName;
            this.status = status;
        }
        
        public Long getUserId() { return userId; }
        public String getUsername() { return username; }
        public String getRole() { return role; }
        public String getRealName() { return realName; }
        public String getStatus() { return status; }
    }

    /**
     * 重置密码响应对象
     */
    static class ResetPasswordResponse {
        private Long userId;
        private String newPassword;
        
        public ResetPasswordResponse(Long userId, String newPassword) {
            this.userId = userId;
            this.newPassword = newPassword;
        }
        
        public Long getUserId() { return userId; }
        public String getNewPassword() { return newPassword; }
    }

    /**
     * 更新状态请求体
     */
    static class UpdateStatusRequest {
        private Boolean status;
        
        public Boolean getStatus() { return status; }
        public void setStatus(Boolean status) { this.status = status; }
    }

    /**
     * 更新状态响应对象
     */
    static class UpdateStatusResponse {
        private Long userId;
        private String status;
        
        public UpdateStatusResponse(Long userId, String status) {
            this.userId = userId;
            this.status = status;
        }
        
        public Long getUserId() { return userId; }
        public String getStatus() { return status; }
    }

    /**
     * 错误响应对象
     */
    static class ErrorResponse {
        private String code;
        private String message;
        
        public ErrorResponse(String code, String message) {
            this.code = code;
            this.message = message;
        }
        
        public String getCode() { return code; }
        public String getMessage() { return message; }
    }

    /**
     * 导入学生账号
     * @param file Excel文件
     * @return 导入结果
     */
    @PostMapping("/import-students")
    public ResponseEntity<?> importStudents(@RequestParam("file") MultipartFile file) {
        try {
            checkAdminRole();
            
            // 检查文件是否为空
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("ERROR", "上传的文件为空"));
            }
            
            // 检查文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("ERROR", "只支持Excel 2007及以上版本（.xlsx）"));
            }
            
            // 调用服务层导入学生
            var result = adminUserManagementService.importStudents(file.getInputStream());
            
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("ERROR", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("ERROR", "导入失败: " + e.getMessage()));
        }
    }
}