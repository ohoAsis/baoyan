package com.example.baoyan_assistant.entity;

import jakarta.persistence.*;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
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
     * 关联学生（多对一关系）
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

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
     * 附件文件列表（以JSON格式存储）
     */
    @Column(columnDefinition = "TEXT")
    private String files;

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
    public Application(Student student, String type, String title, String description, Double points) {
        this.student = student;
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

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
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

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
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