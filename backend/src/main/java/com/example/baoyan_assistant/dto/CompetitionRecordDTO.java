package com.example.baoyan_assistant.dto;

import java.util.List;

public class CompetitionRecordDTO {
    private String id;
    private String name;
    private String level;
    private String award;
    private Boolean isTeamProject;
    private Integer teamRank;
    private String awardDate;
    private Double baseScore;
    private Double computedScore;
    private List<String> evidenceFiles;
    private String createTime;
    private String updateTime;

    // 构造函数
    public CompetitionRecordDTO() {}

    // Getter和Setter方法
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    
    public String getAward() { return award; }
    public void setAward(String award) { this.award = award; }
    
    public Boolean getIsTeamProject() { return isTeamProject; }
    public void setIsTeamProject(Boolean isTeamProject) { this.isTeamProject = isTeamProject; }
    
    public Integer getTeamRank() { return teamRank; }
    public void setTeamRank(Integer teamRank) { this.teamRank = teamRank; }
    
    public String getAwardDate() { return awardDate; }
    public void setAwardDate(String awardDate) { this.awardDate = awardDate; }
    
    public Double getBaseScore() { return baseScore; }
    public void setBaseScore(Double baseScore) { this.baseScore = baseScore; }
    
    public Double getComputedScore() { return computedScore; }
    public void setComputedScore(Double computedScore) { this.computedScore = computedScore; }
    
    public List<String> getEvidenceFiles() { return evidenceFiles; }
    public void setEvidenceFiles(List<String> evidenceFiles) { this.evidenceFiles = evidenceFiles; }
    
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
    
    public String getUpdateTime() { return updateTime; }
    public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }
}