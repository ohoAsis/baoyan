package com.example.baoyan_assistant.service;

import com.example.baoyan_assistant.dto.ApplicationCreateDTO;
import com.example.baoyan_assistant.dto.ApplicationUpdateDTO;
import com.example.baoyan_assistant.entity.Application;
import com.example.baoyan_assistant.entity.FileRecord;
import com.example.baoyan_assistant.entity.Student;
import com.example.baoyan_assistant.repository.ApplicationRepository;
import com.example.baoyan_assistant.repository.StudentRepository;
import com.example.baoyan_assistant.service.FileRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 申请服务类
 * 提供申请信息的业务逻辑处理
 */
@Service
@Transactional
public class ApplicationService {
    
    private static final Logger logger = LoggerFactory.getLogger(ApplicationService.class);
    
    @Autowired
    private ApplicationRepository applicationRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private FileRecordService fileRecordService;
    
    @Autowired
    private com.example.baoyan_assistant.repository.UserRepository userRepository;
    
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
        // 不再验证学生是否存在，直接返回申请列表
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
        // 不再验证学生是否存在，直接返回申请列表
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
        // 不再验证学生是否存在，直接返回申请列表
        return applicationRepository.findByStudentIdAndType(studentId, type);
    }
    
    /**
     * 创建新申请
     * @param dto 创建申请DTO
     * @return 创建的申请对象
     * @throws RuntimeException 如果学生不存在
     */
    public Application create(ApplicationCreateDTO dto) {
        // 记录收到的fileIds
        Long[] fileIds = dto.getFileIds();
        logger.info("Create application, received fileIds: {}", fileIds == null ? "null" : java.util.Arrays.toString(fileIds));
        
        // 从UserContext获取当前登录用户ID
        Long userId = com.example.baoyan_assistant.util.UserContext.getUserId();
        if (userId == null) {
            throw new RuntimeException("未找到当前登录用户信息");
        }
        
        // 查询当前登录用户
        com.example.baoyan_assistant.entity.User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在，ID: " + userId));
        
        // 验证当前用户是否为学生角色
        if (!"student".equals(currentUser.getRole())) {
            throw new RuntimeException("只有学生角色可以提交申请");
        }
        
        // 使用当前用户的用户名作为学号
        String studentId = currentUser.getUsername();
        
        // 创建 Application 并设置学生ID
        Application application = new Application();
        application.setStudentId(studentId);  // 直接设置学生ID，不再关联Student实体
        application.setType(dto.getType());
        application.setTitle(dto.getTitle());
        application.setDescription(dto.getDescription());
        application.setPoints(dto.getPoints());
        application.setStatus("pending");
        application.setSubmittedAt(LocalDateTime.now());
        
        // 保存申请以获取applicationId
        Application savedApplication = applicationRepository.save(application);
        
        // 设置文件列表（如果有）
        if (dto.getFileIds() != null && dto.getFileIds().length > 0) {
            // 根据fileIds查询对应的FileRecord
            List<FileRecord> fileRecords = fileRecordService.findByIds(List.of(dto.getFileIds()));
            
            if (fileRecords.isEmpty()) {
                throw new RuntimeException("未找到指定的文件记录");
            }
            
            // 验证文件记录的上传者是否为当前用户
            for (FileRecord fileRecord : fileRecords) {
                if (!fileRecord.getUploaderId().equals(userId)) {
                    throw new RuntimeException("无权访问其他用户上传的文件");
                }
            }
            
            // 将这些FileRecord.applicationId更新为新建的application.id
            for (FileRecord fileRecord : fileRecords) {
                fileRecord.setApplicationId(savedApplication.getId());
            }
            
            // 保存更新后的FileRecord
            List<FileRecord> updatedFileRecords = fileRecordService.saveAll(fileRecords);
        }
        
        // 保存并返回完整的 Application 对象
        return savedApplication;
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
        
        // 记录旧状态
        String oldStatus = application.getStatus();
        
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
        
        // 保存申请
        Application savedApplication = applicationRepository.save(application);
        
        // 如果申请被审核通过，则同步更新Student表
        if ("approved".equals(savedApplication.getStatus()) && !"approved".equals(oldStatus)) {
            syncStudentScore(savedApplication);
        }
        
        return savedApplication;
    }
    
    /**
     * 同步学生分数到Student表
     * @param application 审核通过的申请
     */
    private void syncStudentScore(Application application) {
        String studentId = application.getStudentId();
        Double points = application.getPoints();
        
        // 根据studentId查找或创建Student
        Student student = studentRepository.findByStudentId(studentId)
                .orElseGet(() -> {
                    // 不存在则创建新的Student
                    Student newStudent = new Student();
                    newStudent.setStudentId(studentId);
                    newStudent.setName(""); // 暂时设置为空，后续可以从User获取或更新
                    newStudent.setTotalScore(0.0);
                    return newStudent;
                });
        
        // 更新总分：累加当前申请的分数
        Double currentTotal = student.getTotalScore() != null ? student.getTotalScore() : 0.0;
        student.setTotalScore(currentTotal + points);
        
        // 更新时间会通过@PreUpdate自动设置
        
        // 保存Student
        studentRepository.save(student);
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
     */
    public void deleteByStudentId(String studentId) {
        // 不再验证学生是否存在，直接删除申请
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
        // 不再验证学生是否存在，直接返回申请
        return applicationRepository.findOptionalByStudentId(studentId);
    }
}