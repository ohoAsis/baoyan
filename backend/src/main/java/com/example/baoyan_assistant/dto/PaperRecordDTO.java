package com.example.baoyan_assistant.dto;

import java.util.List;

public class PaperRecordDTO {
    private String id;
    private String title;
    private String level;
    private String publication;
    private String publishDate;
    private Integer authorOrder;
    private Integer totalAuthors;
    private Boolean unitIsFirstUnit;
    private Double baseScore;
    private Double computedScore;
    private List<String> evidenceFiles;
    private String createTime;
    private String updateTime;

    // 构造函数
    public PaperRecordDTO() {}

    // Getter和Setter方法
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    
    public String getPublication() { return publication; }
    public void setPublication(String publication) { this.publication = publication; }
    
    public String getPublishDate() { return publishDate; }
    public void setPublishDate(String publishDate) { this.publishDate = publishDate; }
    
    public Integer getAuthorOrder() { return authorOrder; }
    public void setAuthorOrder(Integer authorOrder) { this.authorOrder = authorOrder; }
    
    public Integer getTotalAuthors() { return totalAuthors; }
    public void setTotalAuthors(Integer totalAuthors) { this.totalAuthors = totalAuthors; }
    
    public Boolean getUnitIsFirstUnit() { return unitIsFirstUnit; }
    public void setUnitIsFirstUnit(Boolean unitIsFirstUnit) { this.unitIsFirstUnit = unitIsFirstUnit; }
    
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