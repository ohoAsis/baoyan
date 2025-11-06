package com.example.baoyan_assistant.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 专利记录实体类
 * 用于存储学生申请的专利信息
 */
@Entity
@Table(name = "patent_record")
public class PatentRecord {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 专利名称
     */
    @Column(length = 500)
    private String title;

    /**
     * 专利类型（发明专利/实用新型）
     */
    @Column(length = 50)
    private String type;

    /**
     * 授权日期
     */
    @Column
    private LocalDate authorizationDate;

    /**
     * 是否第一发明人
     */
    @Column
    private Boolean isFirstAuthor;

    /**
     * 基础分
     */
    @Column
    private Double baseScore;

    /**
     * 计算分
     */
    @Column
    private Double computedScore;

    /**
     * 证据文件路径
     */
    @Column(length = 500)
    private String evidenceFiles;

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
    public PatentRecord() {
    }

    // ==================== Getter 和 Setter 方法 ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getAuthorizationDate() {
        return authorizationDate;
    }

    public void setAuthorizationDate(LocalDate authorizationDate) {
        this.authorizationDate = authorizationDate;
    }

    public Boolean getIsFirstAuthor() {
        return isFirstAuthor;
    }

    public void setIsFirstAuthor(Boolean isFirstAuthor) {
        this.isFirstAuthor = isFirstAuthor;
    }

    public Double getBaseScore() {
        return baseScore;
    }

    public void setBaseScore(Double baseScore) {
        this.baseScore = baseScore;
    }

    public Double getComputedScore() {
        return computedScore;
    }

    public void setComputedScore(Double computedScore) {
        this.computedScore = computedScore;
    }

    public String getEvidenceFiles() {
        return evidenceFiles;
    }

    public void setEvidenceFiles(String evidenceFiles) {
        this.evidenceFiles = evidenceFiles;
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
        return "PatentRecord{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", authorizationDate=" + authorizationDate +
                ", isFirstAuthor=" + isFirstAuthor +
                ", baseScore=" + baseScore +
                ", computedScore=" + computedScore +
                ", evidenceFiles='" + evidenceFiles + '\'' +
                '}';
    }
}

