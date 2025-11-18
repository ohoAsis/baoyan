package com.example.baoyan_assistant.controller;

import com.example.baoyan_assistant.dto.ApplicationCreateDTO;
import com.example.baoyan_assistant.dto.ApplicationDTO;
import com.example.baoyan_assistant.dto.ApplicationUpdateDTO;
import com.example.baoyan_assistant.entity.Application;
import com.example.baoyan_assistant.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 申请管理控制器
 * 提供申请信息的 RESTful API 接口
 */
@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
public class ApplicationController {
    
    @Autowired
    private ApplicationService applicationService;
    
    /**
     * 获取所有申请
     * GET /api/applications
     * @return 所有申请列表
     */
    @GetMapping
    public ResponseEntity<List<ApplicationDTO>> getAllApplications() {
        List<Application> applications = applicationService.getAll();
        List<ApplicationDTO> applicationDTOs = applications.stream()
                .map(ApplicationDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(applicationDTOs);
    }
    
    /**
     * 根据ID获取申请
     * GET /api/applications/{id}
     * @param id 申请ID
     * @return 申请对象
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationDTO> getApplicationById(@PathVariable Long id) {
        try {
            Application application = applicationService.getById(id);
            return ResponseEntity.ok(ApplicationDTO.fromEntity(application));
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
    public ResponseEntity<List<ApplicationDTO>> getApplicationsByStudentId(@PathVariable Long studentId) {
        List<Application> applications = applicationService.getByStudentId(studentId);
        List<ApplicationDTO> applicationDTOs = applications.stream()
                .map(ApplicationDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(applicationDTOs);
    }
    
    /**
     * 创建新申请
     * POST /api/applications
     * @param createDTO 创建申请DTO
     * @return 创建的申请对象
     */
    @PostMapping
    public ResponseEntity<ApplicationDTO> createApplication(@RequestBody ApplicationCreateDTO createDTO) {
        try {
            Application application = applicationService.create(createDTO);
            return new ResponseEntity<>(ApplicationDTO.fromEntity(application), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 更新申请信息（支持审核）
     * PUT /api/applications/{id}
     * @param id 申请ID
     * @param updateDTO 更新申请DTO
     * @return 更新后的申请对象
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApplicationDTO> updateApplication(
            @PathVariable Long id, 
            @RequestBody ApplicationUpdateDTO updateDTO) {
        try {
            Application application = applicationService.update(id, updateDTO);
            return ResponseEntity.ok(ApplicationDTO.fromEntity(application));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 删除申请
     * DELETE /api/applications/{id}
     * @param id 申请ID
     * @return 无内容
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        try {
            applicationService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}