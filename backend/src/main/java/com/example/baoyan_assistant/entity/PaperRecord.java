package com.example.baoyan_assistant.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 论文记录实体类
 * 用于存储学生发表的论文信息
 */
@Entity
@Table(name = "paper_record")
public class PaperRecord {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 论文标题
     */
    @Column(length = 500)
    private String title;

    /**
     * 论文等级（A/B/C）
     */
    @Column(length = 10)
    private String level;

    /**
     * 发表期刊或会议名称
     */
    @Column(length = 200)
    private String publication;

    /**
     * 发表日期
     */
    @Column
    private LocalDate publishDate;

    /**
     * 作者序号
     */
    @Column
    private Integer authorOrder;

    /**
     * 作者总数
     */
    @Column
    private Integer totalAuthors;

    /**
     * 本校是否为第一单位
     */
    @Column
    private Boolean unitIsFirstUnit;

    /**
     * 基础分
     */
    @Column
    private Double baseScore;

    /**
     * 计算得分
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
    public PaperRecord() {
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPublication() {
        return publication;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public Integer getAuthorOrder() {
        return authorOrder;
    }

    public void setAuthorOrder(Integer authorOrder) {
        this.authorOrder = authorOrder;
    }

    public Integer getTotalAuthors() {
        return totalAuthors;
    }

    public void setTotalAuthors(Integer totalAuthors) {
        this.totalAuthors = totalAuthors;
    }

    public Boolean getUnitIsFirstUnit() {
        return unitIsFirstUnit;
    }

    public void setUnitIsFirstUnit(Boolean unitIsFirstUnit) {
        this.unitIsFirstUnit = unitIsFirstUnit;
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
        return "PaperRecord{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", level='" + level + '\'' +
                ", publication='" + publication + '\'' +
                ", publishDate=" + publishDate +
                ", authorOrder=" + authorOrder +
                ", totalAuthors=" + totalAuthors +
                ", unitIsFirstUnit=" + unitIsFirstUnit +
                ", baseScore=" + baseScore +
                ", computedScore=" + computedScore +
                ", evidenceFiles='" + evidenceFiles + '\'' +
                '}';
    }
}

