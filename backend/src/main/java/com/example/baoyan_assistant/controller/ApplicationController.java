package com.example.baoyan_assistant.controller;

import com.example.baoyan_assistant.dto.ApplicationDTO;
import com.example.baoyan_assistant.dto.DTOConverter;
import com.example.baoyan_assistant.entity.Application;
import com.example.baoyan_assistant.entity.Student;
import com.example.baoyan_assistant.service.ApplicationService;
import com.example.baoyan_assistant.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students/{studentId}/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;
    
    @Autowired
    private StudentService studentService;

    /**
     * 获取学生的所有申请记录
     */
    @GetMapping
    public ResponseEntity<List<ApplicationDTO>> getApplicationsByStudentId(@PathVariable String studentId) {
        List<Application> applications = applicationService.getApplicationsByStudentId(studentId);
        List<ApplicationDTO> applicationDTOs = applications.stream()
                .map(DTOConverter::convertToApplicationDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(applicationDTOs);
    }

    /**
     * 获取学生的特定申请记录
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationDTO> getApplicationById(@PathVariable String studentId, @PathVariable Long id) {
        Application application = applicationService.getApplicationById(id);
        if (application != null && application.getStudent().getStudentId().equals(studentId)) {
            return ResponseEntity.ok(DTOConverter.convertToApplicationDTO(application));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 添加学生的申请记录
     */
    @PostMapping
    public ResponseEntity<ApplicationDTO> addApplication(@PathVariable String studentId, @RequestBody ApplicationDTO applicationDTO) {
        Student student = studentService.getStudentByStudentId(studentId);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        
        Application application = DTOConverter.convertToApplicationEntity(applicationDTO, student);
        Application savedApplication = applicationService.saveApplication(application);
        return ResponseEntity.ok(DTOConverter.convertToApplicationDTO(savedApplication));
    }

    /**
     * 更新学生的申请记录
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApplicationDTO> updateApplication(@PathVariable String studentId, @PathVariable Long id, @RequestBody ApplicationDTO applicationDTO) {
        Application existingApplication = applicationService.getApplicationById(id);
        if (existingApplication == null || !existingApplication.getStudent().getStudentId().equals(studentId)) {
            return ResponseEntity.notFound().build();
        }
        
        Student student = studentService.getStudentByStudentId(studentId);
        Application updatedApplication = DTOConverter.convertToApplicationEntity(applicationDTO, student);
        updatedApplication.setId(id);
        Application savedApplication = applicationService.saveApplication(updatedApplication);
        return ResponseEntity.ok(DTOConverter.convertToApplicationDTO(savedApplication));
    }

    /**
     * 删除学生的申请记录
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable String studentId, @PathVariable Long id) {
        Application application = applicationService.getApplicationById(id);
        if (application != null && application.getStudent().getStudentId().equals(studentId)) {
            applicationService.deleteApplication(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}