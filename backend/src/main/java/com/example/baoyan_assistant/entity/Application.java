package com.example.baoyan_assistant.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 申请实体类
 * 用于存储保研申请信息
 */
@Entity
@Table(name = "application")
public class Application {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 学生ID（使用用户的username作为学号）
     */
    @Column(name = "student_id", nullable = false, length = 50)
    // 当前阶段：studentId 存储学号（username）
    // 未来引入 Student 实体后可演进
    private String studentId;

    /**
     * 申请类型
     */
    @Column(nullable = false, length = 50)
    private String type;

    /**
     * 申请标题
     */
    @Column(nullable = false, length = 200)
    private String title;

    /**
     * 申请描述
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    /**
     * 申请分数
     */
    @Column(nullable = false)
    private Double points;

    /**
     * 申请状态（pending, approved, rejected）
     */
    @Column(nullable = false, length = 20)
    private String status;

    /**
     * 提交时间
     */
    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;

    /**
     * 审核时间
     */
    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    /**
     * 审核评论
     */
    @Column(name = "review_comment", columnDefinition = "TEXT")
    private String reviewComment;

    /**
     * 关联的文件记录列表
     */
    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileRecord> files = new ArrayList<>();

    /**
     * 创建时间
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 默认构造函数
     */
    public Application() {
    }

    /**
     * 带参构造函数
     */
    public Application(String studentId, String type, String title, String description, Double points) {
        this.studentId = studentId;
        this.type = type;
        this.title = title;
        this.description = description;
        this.points = points;
        this.status = "pending";
        this.submittedAt = LocalDateTime.now();
    }

    /**
     * 在持久化前自动设置创建时间和更新时间
     */
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        
        // 如果没有设置提交时间，则使用当前时间
        if (this.submittedAt == null) {
            this.submittedAt = now;
        }
    }

    /**
     * 在更新前自动设置更新时间
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
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

    public List<FileRecord> getFiles() {
        return files;
    }

    public void setFiles(List<FileRecord> files) {
        this.files = files;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}