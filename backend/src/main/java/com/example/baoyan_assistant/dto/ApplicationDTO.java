package com.example.baoyan_assistant.dto;

import java.util.List;

public class ApplicationDTO {
    private String id;
    private String studentId;
    private String studentName;
    private String type;
    private String title;
    private String description;
    private Double points;
    private String status; // pending, approved, rejected
    private String submittedAt;
    private String reviewedAt;
    private String reviewComment;
    private List<String> files;

    // 构造函数
    public ApplicationDTO() {}

    // Getter和Setter方法
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Double getPoints() { return points; }
    public void setPoints(Double points) { this.points = points; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(String submittedAt) { this.submittedAt = submittedAt; }
    
    public String getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(String reviewedAt) { this.reviewedAt = reviewedAt; }
    
    public String getReviewComment() { return reviewComment; }
    public void setReviewComment(String reviewComment) { this.reviewComment = reviewComment; }
    
    public List<String> getFiles() { return files; }
    public void setFiles(List<String> files) { this.files = files; }
}