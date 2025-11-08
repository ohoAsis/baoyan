package com.example.baoyan_assistant.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 竞赛记录实体类
 * 用于存储学生参加的竞赛获奖信息
 */
@Entity
@Table(name = "competition_record")
public class CompetitionRecord {

    int x = 1;

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 竞赛名称
     */
    @Column(length = 200)
    private String name;

    /**
     * 竞赛级别（国际A/B、国家A/B、省A/B等）
     */
    @Column(length = 50)
    private String level;

    /**
     * 获奖等级（特等奖、一、二、三等奖）
     */
    @Column(length = 50)
    private String award;

    /**
     * 是否团队项目
     */
    @Column
    private Boolean isTeamProject;

    /**
     * 在团队中的排名
     */
    @Column
    private Integer teamRank;

    /**
     * 获奖日期
     */
    @Column
    private LocalDate awardDate;

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
    public CompetitionRecord() {
    }

    // ==================== Getter 和 Setter 方法 ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public Boolean getIsTeamProject() {
        return isTeamProject;
    }

    public void setIsTeamProject(Boolean isTeamProject) {
        this.isTeamProject = isTeamProject;
    }

    public Integer getTeamRank() {
        return teamRank;
    }

    public void setTeamRank(Integer teamRank) {
        this.teamRank = teamRank;
    }

    public LocalDate getAwardDate() {
        return awardDate;
    }

    public void setAwardDate(LocalDate awardDate) {
        this.awardDate = awardDate;
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
        return "CompetitionRecord{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", level='" + level + '\'' +
                ", award='" + award + '\'' +
                ", isTeamProject=" + isTeamProject +
                ", teamRank=" + teamRank +
                ", awardDate=" + awardDate +
                ", baseScore=" + baseScore +
                ", computedScore=" + computedScore +
                ", evidenceFiles='" + evidenceFiles + '\'' +
                '}';
    }
}

