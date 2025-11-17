package com.example.baoyan_assistant.dto;

import java.util.List;

public class PatentRecordDTO {
    private String id;
    private String title;
    private String type;
    private String authorizationDate;
    private Boolean isFirstAuthor;
    private Double baseScore;
    private Double computedScore;
    private List<String> evidenceFiles;
    private String createTime;
    private String updateTime;

    // 构造函数
    public PatentRecordDTO() {}

    // Getter和Setter方法
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getAuthorizationDate() { return authorizationDate; }
    public void setAuthorizationDate(String authorizationDate) { this.authorizationDate = authorizationDate; }
    
    public Boolean getIsFirstAuthor() { return isFirstAuthor; }
    public void setIsFirstAuthor(Boolean isFirstAuthor) { this.isFirstAuthor = isFirstAuthor; }
    
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