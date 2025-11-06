package com.example.baoyan_assistant.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 学生实体类
 * 用于存储保研学生的完整信息，包括基本信息、学术成绩、竞赛成绩、综合表现、审核状态等
 */
@Entity
@Table(name = "student")
public class Student {

    // ==================== 基本信息字段 ====================
    
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 学号（唯一）
     */
    @Column(nullable = false, unique = true, length = 20)
    private String studentId;

    /**
     * 姓名
     */
    @Column(nullable = false, length = 50)
    private String name;

    /**
     * 性别
     */
    @Column(length = 10)
    private String gender;

    /**
     * 专业
     */
    @Column(length = 100)
    private String major;

    /**
     * 年级（如"2021级"）
     */
    @Column(length = 20)
    private String grade;

    /**
     * 班级（可选）
     */
    @Column(length = 50)
    private String className;

    /**
     * 平均绩点
     */
    @Column
    private Double gpa;

    /**
     * 学业成绩排名百分比（用于学业综合成绩）
     */
    @Column
    private Double rankingPercent;

    /**
     * 外语成绩（如CET-4、CET-6分数）
     */
    @Column
    private Integer foreignLanguageScore;

    /**
     * 外语类型（如CET-4、TOEFL、IELTS）
     */
    @Column(length = 50)
    private String foreignLanguageType;

    /**
     * 是否满足基本推免条件（如通过外语、无违纪）
     */
    @Column
    private Boolean eligibleForExemption;

    // ==================== 学术专长字段 ====================

    /**
     * A类论文数量
     */
    @Column
    private Integer papersA;

    /**
     * B类论文数量
     */
    @Column
    private Integer papersB;

    /**
     * C类论文数量
     */
    @Column
    private Integer papersC;

    /**
     * 发明专利数量
     */
    @Column
    private Integer patentInventionCount;

    /**
     * 实用新型专利数量
     */
    @Column
    private Integer patentUtilityCount;

    /**
     * 第一作者成果数量
     */
    @Column
    private Integer firstAuthorCount;

    /**
     * CSP成绩
     */
    @Column
    private Integer ccfCspScore;

    /**
     * 学术专长加分（系统计算生成，满分12分）
     */
    @Column
    private Double academicBonus;

    // ==================== 竞赛成绩字段 ====================

    /**
     * 比赛级别（国际A、国际B、国家A、国家B、省A、省B）
     */
    @Column(length = 50)
    private String competitionLevel;

    /**
     * 获奖等级（特等奖、一、二、三等奖等）
     */
    @Column(length = 50)
    private String competitionAward;

    /**
     * 比赛名称
     */
    @Column(length = 200)
    private String competitionName;

    /**
     * 是否为团队项目
     */
    @Column
    private Boolean isTeamProject;

    /**
     * 团队成员排名
     */
    @Column
    private Integer teamRank;

    /**
     * 竞赛加分（系统计算生成）
     */
    @Column
    private Double competitionBonus;

    // ==================== 综合表现字段 ====================

    /**
     * 创新创业项目数量
     */
    @Column
    private Integer innovationProjects;

    /**
     * 最高立项级别（国家、省、校）
     */
    @Column(length = 50)
    private String innovationLevel;

    /**
     * 志愿服务总时长
     */
    @Column
    private Integer volunteerHours;

    /**
     * 荣誉称号（多个用逗号分隔）
     */
    @Column(length = 500)
    private String honorTitles;

    /**
     * 荣誉称号加分
     */
    @Column
    private Double honorBonus;

    /**
     * 社会工作加分
     */
    @Column
    private Double socialWorkBonus;

    /**
     * 志愿服务加分
     */
    @Column
    private Double volunteerBonus;

    /**
     * 体育比赛加分
     */
    @Column
    private Double sportsBonus;

    /**
     * 综合表现总加分（系统计算生成，满分8分）
     */
    @Column
    private Double overallPerformanceBonus;

    // ==================== 综合成绩字段 ====================

    /**
     * 学业综合成绩（占80%）
     */
    @Column
    private Double academicScore;

    /**
     * 学术专长成绩（占12%）
     */
    @Column
    private Double academicSpecialtyScore;

    /**
     * 综合表现成绩（占8%）
     */
    @Column
    private Double comprehensivePerformanceScore;

    /**
     * 总成绩（自动计算）
     */
    @Column
    private Double totalScore;

    /**
     * 是否特殊学术专长通道
     */
    @Column
    private Boolean isSpecialExemption;

    /**
     * 特殊学术专长理由说明
     */
    @Column(length = 500)
    private String specialExemptionReason;

    /**
     * 状态（草稿、已提交、审核中、通过、驳回）
     */
    @Column(length = 20)
    private String applicationStatus;

    // ==================== 审核与公示字段 ====================

    /**
     * 审核状态（未审核、通过、退回修改）
     */
    @Column(length = 20)
    private String reviewStatus;

    /**
     * 审核意见
     */
    @Column(length = 1000)
    private String reviewComments;

    /**
     * 当前公示轮次
     */
    @Column
    private Integer publicityRound;

    /**
     * 是否已公示
     */
    @Column
    private Boolean isPublicized;

    /**
     * 最终排名
     */
    @Column
    private Integer finalRanking;

    /**
     * 是否最终推免通过
     */
    @Column
    private Boolean finalApproved;

    // ==================== 系统字段 ====================

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
    public Student() {
    }

    /**
     * 带基本参数的构造函数
     */
    public Student(String studentId, String name) {
        this.studentId = studentId;
        this.name = name;
    }

    // ==================== Getter 和 Setter 方法 ====================

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }

    public Double getRankingPercent() {
        return rankingPercent;
    }

    public void setRankingPercent(Double rankingPercent) {
        this.rankingPercent = rankingPercent;
    }

    public Integer getForeignLanguageScore() {
        return foreignLanguageScore;
    }

    public void setForeignLanguageScore(Integer foreignLanguageScore) {
        this.foreignLanguageScore = foreignLanguageScore;
    }

    public String getForeignLanguageType() {
        return foreignLanguageType;
    }

    public void setForeignLanguageType(String foreignLanguageType) {
        this.foreignLanguageType = foreignLanguageType;
    }

    public Boolean getEligibleForExemption() {
        return eligibleForExemption;
    }

    public void setEligibleForExemption(Boolean eligibleForExemption) {
        this.eligibleForExemption = eligibleForExemption;
    }

    public Integer getPapersA() {
        return papersA;
    }

    public void setPapersA(Integer papersA) {
        this.papersA = papersA;
    }

    public Integer getPapersB() {
        return papersB;
    }

    public void setPapersB(Integer papersB) {
        this.papersB = papersB;
    }

    public Integer getPapersC() {
        return papersC;
    }

    public void setPapersC(Integer papersC) {
        this.papersC = papersC;
    }

    public Integer getPatentInventionCount() {
        return patentInventionCount;
    }

    public void setPatentInventionCount(Integer patentInventionCount) {
        this.patentInventionCount = patentInventionCount;
    }

    public Integer getPatentUtilityCount() {
        return patentUtilityCount;
    }

    public void setPatentUtilityCount(Integer patentUtilityCount) {
        this.patentUtilityCount = patentUtilityCount;
    }

    public Integer getFirstAuthorCount() {
        return firstAuthorCount;
    }

    public void setFirstAuthorCount(Integer firstAuthorCount) {
        this.firstAuthorCount = firstAuthorCount;
    }

    public Integer getCcfCspScore() {
        return ccfCspScore;
    }

    public void setCcfCspScore(Integer ccfCspScore) {
        this.ccfCspScore = ccfCspScore;
    }

    public Double getAcademicBonus() {
        return academicBonus;
    }

    public void setAcademicBonus(Double academicBonus) {
        this.academicBonus = academicBonus;
    }

    public String getCompetitionLevel() {
        return competitionLevel;
    }

    public void setCompetitionLevel(String competitionLevel) {
        this.competitionLevel = competitionLevel;
    }

    public String getCompetitionAward() {
        return competitionAward;
    }

    public void setCompetitionAward(String competitionAward) {
        this.competitionAward = competitionAward;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
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

    public Double getCompetitionBonus() {
        return competitionBonus;
    }

    public void setCompetitionBonus(Double competitionBonus) {
        this.competitionBonus = competitionBonus;
    }

    public Integer getInnovationProjects() {
        return innovationProjects;
    }

    public void setInnovationProjects(Integer innovationProjects) {
        this.innovationProjects = innovationProjects;
    }

    public String getInnovationLevel() {
        return innovationLevel;
    }

    public void setInnovationLevel(String innovationLevel) {
        this.innovationLevel = innovationLevel;
    }

    public Integer getVolunteerHours() {
        return volunteerHours;
    }

    public void setVolunteerHours(Integer volunteerHours) {
        this.volunteerHours = volunteerHours;
    }

    public String getHonorTitles() {
        return honorTitles;
    }

    public void setHonorTitles(String honorTitles) {
        this.honorTitles = honorTitles;
    }

    public Double getHonorBonus() {
        return honorBonus;
    }

    public void setHonorBonus(Double honorBonus) {
        this.honorBonus = honorBonus;
    }

    public Double getSocialWorkBonus() {
        return socialWorkBonus;
    }

    public void setSocialWorkBonus(Double socialWorkBonus) {
        this.socialWorkBonus = socialWorkBonus;
    }

    public Double getVolunteerBonus() {
        return volunteerBonus;
    }

    public void setVolunteerBonus(Double volunteerBonus) {
        this.volunteerBonus = volunteerBonus;
    }

    public Double getSportsBonus() {
        return sportsBonus;
    }

    public void setSportsBonus(Double sportsBonus) {
        this.sportsBonus = sportsBonus;
    }

    public Double getOverallPerformanceBonus() {
        return overallPerformanceBonus;
    }

    public void setOverallPerformanceBonus(Double overallPerformanceBonus) {
        this.overallPerformanceBonus = overallPerformanceBonus;
    }

    public Double getAcademicScore() {
        return academicScore;
    }

    public void setAcademicScore(Double academicScore) {
        this.academicScore = academicScore;
    }

    public Double getAcademicSpecialtyScore() {
        return academicSpecialtyScore;
    }

    public void setAcademicSpecialtyScore(Double academicSpecialtyScore) {
        this.academicSpecialtyScore = academicSpecialtyScore;
    }

    public Double getComprehensivePerformanceScore() {
        return comprehensivePerformanceScore;
    }

    public void setComprehensivePerformanceScore(Double comprehensivePerformanceScore) {
        this.comprehensivePerformanceScore = comprehensivePerformanceScore;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    public Boolean getIsSpecialExemption() {
        return isSpecialExemption;
    }

    public void setIsSpecialExemption(Boolean isSpecialExemption) {
        this.isSpecialExemption = isSpecialExemption;
    }

    public String getSpecialExemptionReason() {
        return specialExemptionReason;
    }

    public void setSpecialExemptionReason(String specialExemptionReason) {
        this.specialExemptionReason = specialExemptionReason;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public String getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public String getReviewComments() {
        return reviewComments;
    }

    public void setReviewComments(String reviewComments) {
        this.reviewComments = reviewComments;
    }

    public Integer getPublicityRound() {
        return publicityRound;
    }

    public void setPublicityRound(Integer publicityRound) {
        this.publicityRound = publicityRound;
    }

    public Boolean getIsPublicized() {
        return isPublicized;
    }

    public void setIsPublicized(Boolean isPublicized) {
        this.isPublicized = isPublicized;
    }

    public Integer getFinalRanking() {
        return finalRanking;
    }

    public void setFinalRanking(Integer finalRanking) {
        this.finalRanking = finalRanking;
    }

    public Boolean getFinalApproved() {
        return finalApproved;
    }

    public void setFinalApproved(Boolean finalApproved) {
        this.finalApproved = finalApproved;
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
        return "Student{" +
                "id=" + id +
                ", studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", major='" + major + '\'' +
                ", grade='" + grade + '\'' +
                ", gpa=" + gpa +
                ", totalScore=" + totalScore +
                ", applicationStatus='" + applicationStatus + '\'' +
                ", reviewStatus='" + reviewStatus + '\'' +
                ", finalRanking=" + finalRanking +
                '}';
    }
}



