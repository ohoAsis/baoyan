package com.example.baoyan_assistant.dto;

import java.time.LocalDateTime;
import java.util.List;

public class StudentDTO {
    private String id;
    private String studentId;
    private String name;
    private String gender;
    private String major;
    private String grade;
    private String className;
    private Double gpa;
    private Double rankingPercent;
    private Integer foreignLanguageScore;
    private String foreignLanguageType;
    private Boolean eligibleForExemption;
    private Integer papersA;
    private Integer papersB;
    private Integer papersC;
    private Integer patentInventionCount;
    private Integer patentUtilityCount;
    private Integer firstAuthorCount;
    private Integer ccfCspScore;
    private Double academicBonus;
    private String competitionLevel;
    private String competitionAward;
    private String competitionName;
    private Boolean isTeamProject;
    private Integer teamRank;
    private Double competitionBonus;
    private Integer innovationProjects;
    private String innovationLevel;
    private Integer volunteerHours;
    private String honorTitles;
    private Double honorBonus;
    private Double socialWorkBonus;
    private Double volunteerBonus;
    private Double sportsBonus;
    private Double overallPerformanceBonus;
    private Double academicScore;
    private Double academicSpecialtyScore;
    private Double comprehensivePerformanceScore;
    private Double totalScore;
    private Boolean isSpecialExemption;
    private String specialExemptionReason;
    private String applicationStatus;
    private String reviewStatus;
    private String reviewComments;
    private Integer publicityRound;
    private Boolean isPublicized;
    private Integer finalRanking;
    private Boolean finalApproved;
    private String createTime;
    private String updateTime;
    private List<PaperRecordDTO> papers;
    private List<PatentRecordDTO> patents;
    private List<CompetitionRecordDTO> competitions;
    private List<HonorRecordDTO> honors;
    private List<ReviewRecordDTO> reviews;

    // 构造函数、getter和setter方法
    public StudentDTO() {}

    // 省略所有getter和setter方法，实际实现中需要添加
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
    
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    
    public Double getGpa() { return gpa; }
    public void setGpa(Double gpa) { this.gpa = gpa; }
    
    public Double getRankingPercent() { return rankingPercent; }
    public void setRankingPercent(Double rankingPercent) { this.rankingPercent = rankingPercent; }
    
    public Integer getForeignLanguageScore() { return foreignLanguageScore; }
    public void setForeignLanguageScore(Integer foreignLanguageScore) { this.foreignLanguageScore = foreignLanguageScore; }
    
    public String getForeignLanguageType() { return foreignLanguageType; }
    public void setForeignLanguageType(String foreignLanguageType) { this.foreignLanguageType = foreignLanguageType; }
    
    public Boolean getEligibleForExemption() { return eligibleForExemption; }
    public void setEligibleForExemption(Boolean eligibleForExemption) { this.eligibleForExemption = eligibleForExemption; }
    
    public Integer getPapersA() { return papersA; }
    public void setPapersA(Integer papersA) { this.papersA = papersA; }
    
    public Integer getPapersB() { return papersB; }
    public void setPapersB(Integer papersB) { this.papersB = papersB; }
    
    public Integer getPapersC() { return papersC; }
    public void setPapersC(Integer papersC) { this.papersC = papersC; }
    
    public Integer getPatentInventionCount() { return patentInventionCount; }
    public void setPatentInventionCount(Integer patentInventionCount) { this.patentInventionCount = patentInventionCount; }
    
    public Integer getPatentUtilityCount() { return patentUtilityCount; }
    public void setPatentUtilityCount(Integer patentUtilityCount) { this.patentUtilityCount = patentUtilityCount; }
    
    public Integer getFirstAuthorCount() { return firstAuthorCount; }
    public void setFirstAuthorCount(Integer firstAuthorCount) { this.firstAuthorCount = firstAuthorCount; }
    
    public Integer getCcfCspScore() { return ccfCspScore; }
    public void setCcfCspScore(Integer ccfCspScore) { this.ccfCspScore = ccfCspScore; }
    
    public Double getAcademicBonus() { return academicBonus; }
    public void setAcademicBonus(Double academicBonus) { this.academicBonus = academicBonus; }
    
    public String getCompetitionLevel() { return competitionLevel; }
    public void setCompetitionLevel(String competitionLevel) { this.competitionLevel = competitionLevel; }
    
    public String getCompetitionAward() { return competitionAward; }
    public void setCompetitionAward(String competitionAward) { this.competitionAward = competitionAward; }
    
    public String getCompetitionName() { return competitionName; }
    public void setCompetitionName(String competitionName) { this.competitionName = competitionName; }
    
    public Boolean getIsTeamProject() { return isTeamProject; }
    public void setIsTeamProject(Boolean isTeamProject) { this.isTeamProject = isTeamProject; }
    
    public Integer getTeamRank() { return teamRank; }
    public void setTeamRank(Integer teamRank) { this.teamRank = teamRank; }
    
    public Double getCompetitionBonus() { return competitionBonus; }
    public void setCompetitionBonus(Double competitionBonus) { this.competitionBonus = competitionBonus; }
    
    public Integer getInnovationProjects() { return innovationProjects; }
    public void setInnovationProjects(Integer innovationProjects) { this.innovationProjects = innovationProjects; }
    
    public String getInnovationLevel() { return innovationLevel; }
    public void setInnovationLevel(String innovationLevel) { this.innovationLevel = innovationLevel; }
    
    public Integer getVolunteerHours() { return volunteerHours; }
    public void setVolunteerHours(Integer volunteerHours) { this.volunteerHours = volunteerHours; }
    
    public String getHonorTitles() { return honorTitles; }
    public void setHonorTitles(String honorTitles) { this.honorTitles = honorTitles; }
    
    public Double getHonorBonus() { return honorBonus; }
    public void setHonorBonus(Double honorBonus) { this.honorBonus = honorBonus; }
    
    public Double getSocialWorkBonus() { return socialWorkBonus; }
    public void setSocialWorkBonus(Double socialWorkBonus) { this.socialWorkBonus = socialWorkBonus; }
    
    public Double getVolunteerBonus() { return volunteerBonus; }
    public void setVolunteerBonus(Double volunteerBonus) { this.volunteerBonus = volunteerBonus; }
    
    public Double getSportsBonus() { return sportsBonus; }
    public void setSportsBonus(Double sportsBonus) { this.sportsBonus = sportsBonus; }
    
    public Double getOverallPerformanceBonus() { return overallPerformanceBonus; }
    public void setOverallPerformanceBonus(Double overallPerformanceBonus) { this.overallPerformanceBonus = overallPerformanceBonus; }
    
    public Double getAcademicScore() { return academicScore; }
    public void setAcademicScore(Double academicScore) { this.academicScore = academicScore; }
    
    public Double getAcademicSpecialtyScore() { return academicSpecialtyScore; }
    public void setAcademicSpecialtyScore(Double academicSpecialtyScore) { this.academicSpecialtyScore = academicSpecialtyScore; }
    
    public Double getComprehensivePerformanceScore() { return comprehensivePerformanceScore; }
    public void setComprehensivePerformanceScore(Double comprehensivePerformanceScore) { this.comprehensivePerformanceScore = comprehensivePerformanceScore; }
    
    public Double getTotalScore() { return totalScore; }
    public void setTotalScore(Double totalScore) { this.totalScore = totalScore; }
    
    public Boolean getIsSpecialExemption() { return isSpecialExemption; }
    public void setIsSpecialExemption(Boolean isSpecialExemption) { this.isSpecialExemption = isSpecialExemption; }
    
    public String getSpecialExemptionReason() { return specialExemptionReason; }
    public void setSpecialExemptionReason(String specialExemptionReason) { this.specialExemptionReason = specialExemptionReason; }
    
    public String getApplicationStatus() { return applicationStatus; }
    public void setApplicationStatus(String applicationStatus) { this.applicationStatus = applicationStatus; }
    
    public String getReviewStatus() { return reviewStatus; }
    public void setReviewStatus(String reviewStatus) { this.reviewStatus = reviewStatus; }
    
    public String getReviewComments() { return reviewComments; }
    public void setReviewComments(String reviewComments) { this.reviewComments = reviewComments; }
    
    public Integer getPublicityRound() { return publicityRound; }
    public void setPublicityRound(Integer publicityRound) { this.publicityRound = publicityRound; }
    
    public Boolean getIsPublicized() { return isPublicized; }
    public void setIsPublicized(Boolean isPublicized) { this.isPublicized = isPublicized; }
    
    public Integer getFinalRanking() { return finalRanking; }
    public void setFinalRanking(Integer finalRanking) { this.finalRanking = finalRanking; }
    
    public Boolean getFinalApproved() { return finalApproved; }
    public void setFinalApproved(Boolean finalApproved) { this.finalApproved = finalApproved; }
    
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
    
    public String getUpdateTime() { return updateTime; }
    public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }
    
    public List<PaperRecordDTO> getPapers() { return papers; }
    public void setPapers(List<PaperRecordDTO> papers) { this.papers = papers; }
    
    public List<PatentRecordDTO> getPatents() { return patents; }
    public void setPatents(List<PatentRecordDTO> patents) { this.patents = patents; }
    
    public List<CompetitionRecordDTO> getCompetitions() { return competitions; }
    public void setCompetitions(List<CompetitionRecordDTO> competitions) { this.competitions = competitions; }
    
    public List<HonorRecordDTO> getHonors() { return honors; }
    public void setHonors(List<HonorRecordDTO> honors) { this.honors = honors; }
    
    public List<ReviewRecordDTO> getReviews() { return reviews; }
    public void setReviews(List<ReviewRecordDTO> reviews) { this.reviews = reviews; }
}