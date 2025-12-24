package com.example.baoyan_assistant.service;

import com.example.baoyan_assistant.entity.User;
import com.example.baoyan_assistant.repository.UserRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
     * 创建管理员用户
     * @param username 用户名
     * @param realName 真实姓名
     * @return 创建的用户对象
     * @throws RuntimeException 如果用户名已存在
     */
    public User createAdminUser(String username, String realName) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("用户名已存在: " + username);
        }

        // 使用固定初始密码并加密
        String rawPassword = "123456";
        String encryptedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptedPassword);
        user.setRole("admin");
        user.setRealName(realName);
        user.setStatus("active");

        return userRepository.save(user);
    }

    /**
     * 生成随机密码（8-12位）
     * @return 随机密码字符串
     */
    private String generateRandomPassword() {
        // 生成8-12位随机密码
        int length = 8 + (int) (Math.random() * 5);
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return password.toString();
    }
    
    /**
     * 辅助方法：将Excel单元格值转换为字符串，处理不同类型
     * @param cell Excel单元格
     * @return 字符串值
     */
    private String getStringCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // 处理数字类型，特别是学号等长数字，避免科学计数法
                cell.setCellType(CellType.STRING);
                return cell.getStringCellValue();
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    cell.setCellType(CellType.STRING);
                    return cell.getStringCellValue();
                } catch (Exception e) {
                    return "";
                }
            default:
                return "";
        }
    }

    /**
     * 查询用户列表
     * @param role 角色（可选）
     * @param keyword 关键字（可选，模糊匹配用户名、真实姓名、学号或工号）
     * @return 用户列表
     */
    public List<User> listUsers(Optional<String> role, Optional<String> keyword) {
        String roleValue = role.orElse(null);
        String keywordValue = keyword.orElse(null);
        return userRepository.findByRoleAndKeyword(roleValue, keywordValue);
    }

    /**
     * 重置用户密码
     * @param userId 用户ID
     * @return 生成的明文新密码
     */
    public String resetPassword(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在: " + userId));

        // 生成新密码并加密
        String newPassword = generateRandomPassword();
        String encryptedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        // 更新密码和更新时间
        user.setPassword(encryptedPassword);
        user.setUpdateTime(LocalDateTime.now());
        userRepository.save(user);

        return newPassword;
    }

    /**
     * 更新用户状态
     * @param userId 用户ID
     * @param status 新状态（active/disabled）
     */
    public void updateStatus(Long userId, String status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在: " + userId));

        // 验证状态值
        if (!"active".equals(status) && !"disabled".equals(status)) {
            throw new IllegalArgumentException("无效的状态值: " + status + ", 只能是 active 或 disabled");
        }

        // 更新状态和更新时间
        user.setStatus(status);
        user.setUpdateTime(LocalDateTime.now());
        userRepository.save(user);
    }

    /**
     * 更新createStudentUser方法，使用IllegalArgumentException抛出错误
     */
    public User createStudentUser(String studentId, String realName) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(studentId)) {
            throw new IllegalArgumentException("用户名已存在: " + studentId);
        }

        // 使用固定初始密码并加密
        String rawPassword = "123456";
        String encryptedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

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
     * 更新createReviewerUser方法，使用IllegalArgumentException抛出错误
     */
    public User createReviewerUser(String teacherId, String realName) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(teacherId)) {
            throw new IllegalArgumentException("用户名已存在: " + teacherId);
        }

        // 使用固定初始密码并加密
        String rawPassword = "123456";
        String encryptedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

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
     * 导入学生列表结果
     */
    public static class ImportResult {
        private int total;
        private int success;
        private int failed;
        private List<ImportFailure> failures;
        
        // 构造函数、getter和setter
        public ImportResult() {
            this.failures = new ArrayList<>();
        }
        
        public int getTotal() { return total; }
        public void setTotal(int total) { this.total = total; }
        public int getSuccess() { return success; }
        public void setSuccess(int success) { this.success = success; }
        public int getFailed() { return failed; }
        public void setFailed(int failed) { this.failed = failed; }
        public List<ImportFailure> getFailures() { return failures; }
        public void setFailures(List<ImportFailure> failures) { this.failures = failures; }
    }

    /**
     * 导入失败记录
     */
    public static class ImportFailure {
        private int row;
        private String studentId;
        private String reason;
        
        // 构造函数、getter和setter
        public ImportFailure(int row, String studentId, String reason) {
            this.row = row;
            this.studentId = studentId;
            this.reason = reason;
        }
        
        public int getRow() { return row; }
        public void setRow(int row) { this.row = row; }
        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }

    /**
     * 导入学生账号
     * @param inputStream Excel文件输入流
     * @return 导入结果
     */
    public ImportResult importStudents(InputStream inputStream) {
        ImportResult result = new ImportResult();
        
        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            XSSFSheet sheet = workbook.getSheetAt(0); // 获取第一个工作表
            
            // 记录总行数（包括表头）
            int totalRows = sheet.getLastRowNum() + 1;
            System.out.println("Excel总行数: " + totalRows);
            
            // 遍历所有行
            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                result.setTotal(result.getTotal() + 1);
                
                // 智能识别表头（仅对第一行）
                if (i == 0) {
                    String firstCellValue = getStringCellValue(row.getCell(0)).trim();
                    // 如果第一行包含"学号"或不是纯数字，则判定为表头，跳过
                    if (firstCellValue.contains("学号") || !firstCellValue.matches("\\d+")) {
                        System.out.println("跳过表头行: " + (i + 1) + ", 值: " + firstCellValue);
                        continue;
                    }
                    System.out.println("无表头，直接处理第一行");
                }
                
                try {
                    // 只从第一列获取学号
                    String studentId = getStringCellValue(row.getCell(0)).trim();
                    
                    // 检查学号是否有效
                    if (studentId.isEmpty()) {
                        result.setFailed(result.getFailed() + 1);
                        result.getFailures().add(new ImportFailure(
                                row.getRowNum() + 1, 
                                studentId, 
                                "学号不能为空"
                        ));
                        continue;
                    }
                    
                    // 创建学生用户，真实姓名使用学号作为默认值
                    createStudentUser(studentId, studentId);
                    result.setSuccess(result.getSuccess() + 1);
                } catch (IllegalArgumentException e) {
                    // 捕获用户名已存在等错误
                    result.setFailed(result.getFailed() + 1);
                    result.getFailures().add(new ImportFailure(
                            row.getRowNum() + 1, 
                            row.getCell(0) != null ? row.getCell(0).toString() : "", 
                            e.getMessage()
                    ));
                } catch (Exception e) {
                    // 捕获其他异常
                    result.setFailed(result.getFailed() + 1);
                    result.getFailures().add(new ImportFailure(
                            row.getRowNum() + 1, 
                            row.getCell(0) != null ? row.getCell(0).toString() : "", 
                            "处理失败: " + e.getMessage()
                    ));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("读取Excel文件失败: " + e.getMessage());
        }
        
        // 检查是否有成功处理的记录
        if (result.getSuccess() == 0) {
            throw new RuntimeException("Excel 中未检测到有效学号数据");
        }
        
        System.out.println("最终导入结果 - 成功: " + result.getSuccess() + ", 失败: " + result.getFailed());
        return result;
    }
}