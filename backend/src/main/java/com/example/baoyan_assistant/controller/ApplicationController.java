package com.example.baoyan_assistant.controller;

import com.example.baoyan_assistant.dto.ApplicationCreateDTO;
import com.example.baoyan_assistant.dto.ApplicationDTO;
import com.example.baoyan_assistant.dto.ApplicationUpdateDTO;
import com.example.baoyan_assistant.entity.Application;
import com.example.baoyan_assistant.entity.FileRecord;
import com.example.baoyan_assistant.service.ApplicationService;
import com.example.baoyan_assistant.service.FileRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 申请管理控制器
 * 提供申请信息的 RESTful API 接口
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApplicationController {
    
    @Autowired
    private ApplicationService applicationService;
    
    @Autowired
    private FileRecordService fileRecordService;
    
    /**
     * 获取所有申请
     * GET /api/applications
     * @return 所有申请列表
     */
    @GetMapping("/applications")
    public ResponseEntity<List<ApplicationDTO>> getAllApplications() {
        List<Application> applications = applicationService.getAll();
        List<ApplicationDTO> applicationDTOs = applications.stream()
                .map(application -> {
                    // 显式查询关联的FileRecord
                    List<FileRecord> files = fileRecordService.getByApplicationId(application.getId());
                    return ApplicationDTO.fromEntity(application, files);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(applicationDTOs);
    }
    
    /**
     * 根据ID获取申请
     * GET /api/applications/{id}
     * @param id 申请ID
     * @return 申请对象
     */
    @GetMapping("/applications/{id}")
    public ResponseEntity<ApplicationDTO> getApplicationById(@PathVariable Long id) {
        try {
            Application application = applicationService.getById(id);
            // 显式查询关联的FileRecord
            List<FileRecord> files = fileRecordService.getByApplicationId(application.getId());
            return ResponseEntity.ok(ApplicationDTO.fromEntity(application, files));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 根据学生ID获取申请列表
     * GET /api/students/{studentId}/applications
     * @param studentId 学生ID
     * @return 申请列表
     */
    @GetMapping("/students/{studentId}/applications")
    public ResponseEntity<List<ApplicationDTO>> getApplicationsByStudentId(@PathVariable String studentId) {
        try {
            List<Application> applications = applicationService.getByStudentId(studentId);
            List<ApplicationDTO> applicationDTOs = applications.stream()
                    .map(application -> {
                        // 显式查询关联的FileRecord
                        List<FileRecord> files = fileRecordService.getByApplicationId(application.getId());
                        return ApplicationDTO.fromEntity(application, files);
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(applicationDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 根据学生ID和状态获取申请列表
     * GET /api/students/{studentId}/applications?status={status}
     * @param studentId 学生ID
     * @param status 申请状态
     * @return 申请列表
     */
    @GetMapping("/students/{studentId}/applications/filter")
    public ResponseEntity<List<ApplicationDTO>> getApplicationsByStudentIdAndStatus(
            @PathVariable String studentId,
            @RequestParam String status) {
        try {
            List<Application> applications = applicationService.getByStudentIdAndStatus(studentId, status);
            List<ApplicationDTO> applicationDTOs = applications.stream()
                    .map(application -> {
                        // 显式查询关联的FileRecord
                        List<FileRecord> files = fileRecordService.getByApplicationId(application.getId());
                        return ApplicationDTO.fromEntity(application, files);
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(applicationDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 创建新申请
     * POST /api/applications
     * @param createDTO 创建申请DTO
     * @return 创建的申请对象
     */
    @PostMapping("/applications")
    public ResponseEntity<?> createApplication(@RequestBody ApplicationCreateDTO createDTO) {
        try {
            System.out.println("收到创建申请请求");
            System.out.println("请求体: " + (createDTO != null ? 
                "studentId=" + createDTO.getStudentId() + 
                ", type=" + createDTO.getType() + 
                ", title=" + createDTO.getTitle() : "null"));
            
            // 验证请求体
            if (createDTO == null) {
                System.out.println("错误: 请求体为空");
                return ResponseEntity.badRequest().body(Map.of("error", "请求体不能为空"));
            }
            
            Application application = applicationService.create(createDTO);
            System.out.println("创建成功 - 申请ID: " + application.getId());
            return new ResponseEntity<>(ApplicationDTO.fromEntity(application), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            System.out.println("运行时异常: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage() != null ? e.getMessage() : "创建申请失败"));
        } catch (Exception e) {
            System.out.println("异常: " + e.getMessage());
            e.printStackTrace();
            // 检查是否是数据库只读错误
            String errorMsg = e.getMessage() != null ? e.getMessage() : "未知错误";
            if (errorMsg.contains("readonly") || errorMsg.contains("read-only")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "数据库只读错误，请检查数据库文件权限"));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "创建申请失败: " + errorMsg));
        }
    }
    
    /**
     * 更新申请信息（支持审核）
     * PUT /api/applications/{id}
     * @param id 申请ID
     * @param updateDTO 更新申请DTO
     * @return 更新后的申请对象
     */
    @PutMapping("/applications/{id}")
    public ResponseEntity<?> updateApplication(
            @PathVariable Long id, 
            @RequestBody ApplicationUpdateDTO updateDTO) {
        try {
            System.out.println("收到更新申请请求 - ID: " + id);
            System.out.println("请求体: " + (updateDTO != null ? updateDTO.getStatus() + ", " + updateDTO.getReviewComment() : "null"));
            
            // 验证请求体
            if (updateDTO == null) {
                System.out.println("错误: 请求体为空");
                return ResponseEntity.badRequest().body(Map.of("error", "请求体不能为空"));
            }
            
            // 验证状态
            if (updateDTO.getStatus() == null || updateDTO.getStatus().trim().isEmpty()) {
                System.out.println("错误: 申请状态为空");
                return ResponseEntity.badRequest().body(Map.of("error", "申请状态不能为空"));
            }
            
            Application application = applicationService.update(id, updateDTO);
            System.out.println("更新成功 - 申请ID: " + id + ", 状态: " + application.getStatus());
            return ResponseEntity.ok(ApplicationDTO.fromEntity(application));
        } catch (RuntimeException e) {
            System.out.println("运行时异常: " + e.getMessage());
            e.printStackTrace();
            // 如果是"申请不存在"的错误，返回404
            if (e.getMessage() != null && e.getMessage().contains("申请不存在")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", e.getMessage()));
            }
            // 其他运行时异常返回500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage() != null ? e.getMessage() : "更新申请失败"));
        } catch (Exception e) {
            System.out.println("异常: " + e.getMessage());
            e.printStackTrace();
            // 检查是否是数据库只读错误
            String errorMsg = e.getMessage() != null ? e.getMessage() : "未知错误";
            if (errorMsg.contains("readonly") || errorMsg.contains("read-only")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "数据库只读错误，请检查数据库文件权限"));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "更新申请失败: " + errorMsg));
        }
    }
    
    /**
     * 删除申请
     * DELETE /api/applications/{id}
     * @param id 申请ID
     * @return 无内容
     */
    @DeleteMapping("/applications/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        try {
            applicationService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 根据学生ID删除所有申请
     * DELETE /api/students/{studentId}/applications
     * @param studentId 学生ID
     * @return 无内容
     */
    @DeleteMapping("/students/{studentId}/applications")
    public ResponseEntity<Void> deleteApplicationsByStudentId(@PathVariable String studentId) {
        try {
            applicationService.deleteByStudentId(studentId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}