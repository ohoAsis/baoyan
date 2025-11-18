package com.example.baoyan_assistant.service;

import com.example.baoyan_assistant.dto.ApplicationCreateDTO;
import com.example.baoyan_assistant.dto.ApplicationUpdateDTO;
import com.example.baoyan_assistant.entity.Application;
import com.example.baoyan_assistant.entity.Student;
import com.example.baoyan_assistant.repository.ApplicationRepository;
import com.example.baoyan_assistant.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 申请服务类
 * 提供申请信息的业务逻辑处理
 */
@Service
@Transactional
public class ApplicationService {
    
    @Autowired
    private ApplicationRepository applicationRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    /**
     * 获取所有申请
     * @return 所有申请列表
     */
    @Transactional(readOnly = true)
    public List<Application> getAll() {
        return applicationRepository.findAll();
    }
    
    /**
     * 根据ID获取申请
     * @param id 申请ID
     * @return 申请对象
     * @throws RuntimeException 如果申请不存在
     */
    @Transactional(readOnly = true)
    public Application getById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("申请不存在，ID: " + id));
    }
    
    /**
     * 根据学生ID获取申请列表
     * @param studentId 学生ID
     * @return 申请列表
     */
    @Transactional(readOnly = true)
    public List<Application> getByStudentId(Long studentId) {
        return applicationRepository.findByStudentId(studentId);
    }
    
    /**
     * 创建新申请
     * @param dto 创建申请DTO
     * @return 创建的申请对象
     * @throws RuntimeException 如果学生不存在
     */
    public Application create(ApplicationCreateDTO dto) {
        // 查找学生
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("学生不存在，ID: " + dto.getStudentId()));
        
        // 创建申请对象
        Application application = new Application();
        application.setStudent(student);
        application.setType(dto.getType());
        application.setTitle(dto.getTitle());
        application.setDescription(dto.getDescription());
        application.setPoints(dto.getPoints());
        application.setStatus("pending");
        application.setSubmittedAt(LocalDateTime.now());
        
        // 设置文件列表（如果有）
        if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {
            application.setFiles(String.join(",", dto.getFiles()));
        }
        
        return applicationRepository.save(application);
    }
    
    /**
     * 更新申请信息（支持审核）
     * @param id 申请ID
     * @param dto 更新申请DTO
     * @return 更新后的申请对象
     * @throws RuntimeException 如果申请不存在
     */
    public Application update(Long id, ApplicationUpdateDTO dto) {
        // 查找申请
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("申请不存在，ID: " + id));
        
        // 更新状态和审核评论
        if (dto.getStatus() != null) {
            String oldStatus = application.getStatus();
            application.setStatus(dto.getStatus());
            
            // 如果状态从pending变为其他状态，设置审核时间
            if (!"pending".equals(dto.getStatus()) && "pending".equals(oldStatus)) {
                application.setReviewedAt(LocalDateTime.now());
            }
        }
        
        if (dto.getReviewComment() != null) {
            application.setReviewComment(dto.getReviewComment());
        }
        
        return applicationRepository.save(application);
    }
    
    /**
     * 删除申请
     * @param id 申请ID
     * @throws RuntimeException 如果申请不存在
     */
    public void delete(Long id) {
        if (!applicationRepository.existsById(id)) {
            throw new RuntimeException("申请不存在，ID: " + id);
        }
        applicationRepository.deleteById(id);
    }
}