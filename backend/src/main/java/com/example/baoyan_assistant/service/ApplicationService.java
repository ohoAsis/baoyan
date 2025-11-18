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
    public List<Application> getByStudentId(String studentId) {
        // 验证学生是否存在
        if (!studentRepository.existsByStudentId(studentId)) {
            throw new RuntimeException("学生不存在，学号: " + studentId);
        }
        
        return applicationRepository.findByStudentId(studentId);
    }
    
    /**
     * 根据学生ID和状态获取申请列表
     * @param studentId 学生ID
     * @param status 申请状态
     * @return 申请列表
     */
    @Transactional(readOnly = true)
    public List<Application> getByStudentIdAndStatus(String studentId, String status) {
        // 验证学生是否存在
        if (!studentRepository.existsByStudentId(studentId)) {
            throw new RuntimeException("学生不存在，学号: " + studentId);
        }
        
        return applicationRepository.findByStudentIdAndStatus(studentId, status);
    }
    
    /**
     * 根据学生ID和申请类型获取申请列表
     * @param studentId 学生ID
     * @param type 申请类型
     * @return 申请列表
     */
    @Transactional(readOnly = true)
    public List<Application> getByStudentIdAndType(String studentId, String type) {
        // 验证学生是否存在
        if (!studentRepository.existsByStudentId(studentId)) {
            throw new RuntimeException("学生不存在，学号: " + studentId);
        }
        
        return applicationRepository.findByStudentIdAndType(studentId, type);
    }
    
    /**
     * 创建新申请
     * @param dto 创建申请DTO
     * @return 创建的申请对象
     * @throws RuntimeException 如果学生不存在
     */
    public Application create(ApplicationCreateDTO dto) {
        // 根据 dto.studentId 查询学生
        Student student = studentRepository.findByStudentId(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("学生不存在，学号: " + dto.getStudentId()));
        
        // 创建 Application 并设置学生对象
        Application application = new Application();
        application.setStudent(student);  // 必须设置学生对象，而不是 studentId
        application.setType(dto.getType());
        application.setTitle(dto.getTitle());
        application.setDescription(dto.getDescription());
        application.setPoints(dto.getPoints());
        application.setStatus("pending");
        application.setSubmittedAt(LocalDateTime.now());
        
        // 设置文件列表（如果有）
        if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {
            application.setFiles(String.join(",", dto.getFiles()));
        } else {
            application.setFiles("");
        }
        
        // 保存并返回完整的 Application 对象
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
        // 验证输入参数
        if (dto == null) {
            throw new RuntimeException("更新信息不能为空");
        }
        
        // 查找申请
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("申请不存在，ID: " + id));
        
        // 更新状态
        if (dto.getStatus() != null && !dto.getStatus().trim().isEmpty()) {
            application.setStatus(dto.getStatus());
        }
        
        // 更新审核评论
        if (dto.getReviewComment() != null && !dto.getReviewComment().trim().isEmpty()) {
            application.setReviewComment(dto.getReviewComment());
        }
        
        // 每次更新都设置审核时间
        application.setReviewedAt(LocalDateTime.now());
        
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
    
    /**
     * 根据学生ID删除所有申请
     * @param studentId 学生ID
     * @throws RuntimeException 如果学生不存在
     */
    public void deleteByStudentId(String studentId) {
        // 验证学生是否存在
        if (!studentRepository.existsByStudentId(studentId)) {
            throw new RuntimeException("学生不存在，学号: " + studentId);
        }
        
        List<Application> applications = applicationRepository.findByStudentId(studentId);
        for (Application application : applications) {
            applicationRepository.delete(application);
        }
    }
    
    /**
     * 根据学生ID获取单个申请（可选）
     * @param studentId 学生ID
     * @return 申请对象（可选）
     */
    @Transactional(readOnly = true)
    public Optional<Application> getOptionalByStudentId(String studentId) {
        // 验证学生是否存在
        if (!studentRepository.existsByStudentId(studentId)) {
            throw new RuntimeException("学生不存在，学号: " + studentId);
        }
        
        return applicationRepository.findOptionalByStudentId(studentId);
    }
}