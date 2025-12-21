package com.example.baoyan_assistant.dto;

import com.example.baoyan_assistant.entity.Application;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 申请信息DTO
 * 用于将Application实体转换为符合前端要求的JSON格式
 */
public class ApplicationDTO {
    
    private Long id;
    private String studentId;
    private String studentName;
    private String type;
    private String title;
    private String description;
    private Double points;
    private String status;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submittedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reviewedAt;
    
    private String reviewComment;
    private List<String> files;
    
    // 默认构造函数
    public ApplicationDTO() {
    }
    
    // 从实体转换为DTO的静态方法
    public static ApplicationDTO fromEntity(Application application) {
        ApplicationDTO dto = new ApplicationDTO();
        
        // 直接设置Long类型的id
        dto.setId(application.getId());
        
        // 设置学生ID和学生姓名
        if (application.getStudent() != null) {
            dto.setStudentId(application.getStudent().getStudentId());
            dto.setStudentName(application.getStudent().getName());
        }
        
        // 复制其他属性
        dto.setType(application.getType());
        dto.setTitle(application.getTitle());
        dto.setDescription(application.getDescription());
        dto.setPoints(application.getPoints());
        dto.setStatus(application.getStatus());
        dto.setSubmittedAt(application.getSubmittedAt());
        dto.setReviewedAt(application.getReviewedAt());
        dto.setReviewComment(application.getReviewComment());
        
        // 处理文件列表
        if (application.getFiles() != null && !application.getFiles().isEmpty()) {
            List<String> fileUrls = application.getFiles().stream()
                    .map(fileRecord -> fileRecord.getFileUrl())
                    .collect(Collectors.toList());
            dto.setFiles(fileUrls);
        } else {
            dto.setFiles(new ArrayList<>());
        }
        
        return dto;
    }
    
    // 从DTO转换为实体的静态方法
    public static Application toEntity(ApplicationDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Application application = new Application();
        
        // 设置ID
        application.setId(dto.getId());
        
        // 复制其他属性
        application.setType(dto.getType());
        application.setTitle(dto.getTitle());
        application.setDescription(dto.getDescription());
        application.setPoints(dto.getPoints());
        application.setStatus(dto.getStatus());
        application.setSubmittedAt(dto.getSubmittedAt());
        application.setReviewedAt(dto.getReviewedAt());
        application.setReviewComment(dto.getReviewComment());
        
        // 处理文件列表：在toEntity方法中不直接设置文件记录，
        // 因为FileRecord需要与ApplicationId关联，而此时Application可能还没有ID
        application.setFiles(new ArrayList<>());
        
        return application;
    }
    
    // Getter和Setter方法
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public String getStudentName() {
        return studentName;
    }
    
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Double getPoints() {
        return points;
    }
    
    public void setPoints(Double points) {
        this.points = points;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }
    
    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
    
    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }
    
    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }
    
    public String getReviewComment() {
        return reviewComment;
    }
    
    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }
    
    public List<String> getFiles() {
        return files;
    }
    
    public void setFiles(List<String> files) {
        this.files = files;
    }
}