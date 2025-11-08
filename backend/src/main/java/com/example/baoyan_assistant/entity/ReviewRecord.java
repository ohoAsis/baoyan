package com.example.baoyan_assistant.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 审核记录实体类
 * 用于存储学生申请材料的审核记录信息
 */
@Entity
@Table(name = "review_record")
public class ReviewRecord {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 审核人姓名
     */
    @Column(length = 100)
    private String reviewerName;

    /**
     * 审核意见
     */
    @Column(length = 1000)
    private String comments;

    /**
     * 状态（未审核/通过/退回修改）
     */
    @Column(length = 20)
    private String status;

    /**
     * 审核日期
     */
    @Column
    private LocalDate reviewDate;

    /**
     * 审核轮次
     */
    @Column
    private Integer round;

    /**
     * 所属学生（多对一关联）
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(nullable = false)
    private LocalDateTime updateTime;

    // ==================== JPA生命周期回调 ====================

    /**
     * 在插入前自动设置创建时间和更新时间
     */
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    /**
     * 在更新前自动设置更新时间
     */
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

    // ==================== 构造函数 ====================

    /**
     * 无参构造函数（JPA必需）
     */
    public ReviewRecord() {
    }

    // ==================== Getter 和 Setter 方法 ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "ReviewRecord{" +
                "id=" + id +
                ", reviewerName='" + reviewerName + '\'' +
                ", comments='" + comments + '\'' +
                ", status='" + status + '\'' +
                ", reviewDate=" + reviewDate +
                ", round=" + round +
                '}';
    }
}

