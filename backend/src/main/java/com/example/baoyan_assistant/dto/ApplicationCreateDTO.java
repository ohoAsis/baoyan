package com.example.baoyan_assistant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

/**
 * 创建申请DTO
 * 用于接收创建申请的请求数据
 */
public class ApplicationCreateDTO {
    
    @NotBlank(message = "学生ID不能为空")
    private String studentId;
    
    @NotBlank(message = "申请类型不能为空")
    private String type;
    
    @NotBlank(message = "申请标题不能为空")
    private String title;
    
    @NotBlank(message = "申请描述不能为空")
    private String description;
    
    @NotNull(message = "申请分数不能为空")
    @Positive(message = "申请分数必须为正数")
    private Double points;
    
    private List<String> files;
    
    // 默认构造函数
    public ApplicationCreateDTO() {
    }
    
    // 带参构造函数
    public ApplicationCreateDTO(String studentId, String type, String title, String description, Double points) {
        this.studentId = studentId;
        this.type = type;
        this.title = title;
        this.description = description;
        this.points = points;
    }
    
    // Getter和Setter方法
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
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
    
    public List<String> getFiles() {
        return files;
    }
    
    public void setFiles(List<String> files) {
        this.files = files;
    }
}