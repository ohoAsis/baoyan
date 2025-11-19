package com.example.baoyan_assistant.dto;

import com.example.baoyan_assistant.entity.PaperRecord;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 论文记录DTO
 * 用于统一JSON输出格式
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaperRecordDTO {
    
    private Long id;
    
    private String studentId;
    
    private String title;
    
    private String level;
    
    private String publication;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishDate;
    
    private Integer authorOrder;
    
    private Integer totalAuthors;
    
    private Boolean unitIsFirstUnit;
    
    private Double baseScore;
    
    private Double computedScore;
    
    private List<String> evidenceFiles;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updateTime;
    
    // 默认构造函数
    public PaperRecordDTO() {
    }
    
    // 从实体转换为DTO
    public static PaperRecordDTO fromEntity(PaperRecord paperRecord) {
        PaperRecordDTO dto = new PaperRecordDTO();
        dto.setId(paperRecord.getId());
        dto.setTitle(paperRecord.getTitle());
        dto.setLevel(paperRecord.getLevel());
        dto.setPublication(paperRecord.getPublication());
        dto.setPublishDate(paperRecord.getPublishDate());
        dto.setAuthorOrder(paperRecord.getAuthorOrder());
        dto.setTotalAuthors(paperRecord.getTotalAuthors());
        dto.setUnitIsFirstUnit(paperRecord.getUnitIsFirstUnit());
        dto.setBaseScore(paperRecord.getBaseScore());
        dto.setComputedScore(paperRecord.getComputedScore());
        
        // 处理证据文件
        if (paperRecord.getEvidenceFiles() != null && !paperRecord.getEvidenceFiles().isEmpty()) {
            // 使用Arrays.stream(...).toList()方法转换
            dto.setEvidenceFiles(Arrays.stream(paperRecord.getEvidenceFiles().split(",")).toList());
        } else {
            dto.setEvidenceFiles(new ArrayList<>());
        }
        
        dto.setCreateTime(paperRecord.getCreateTime());
        dto.setUpdateTime(paperRecord.getUpdateTime());
        
        // 设置学生ID
        if (paperRecord.getStudent() != null) {
            dto.setStudentId(paperRecord.getStudent().getStudentId());
        }
        
        return dto;
    }
    
    // 将DTO转换为实体
    public PaperRecord toEntity() {
        PaperRecord paperRecord = new PaperRecord();
        paperRecord.setId(this.id);
        paperRecord.setTitle(this.title);
        paperRecord.setLevel(this.level);
        paperRecord.setPublication(this.publication);
        paperRecord.setPublishDate(this.publishDate);
        paperRecord.setAuthorOrder(this.authorOrder);
        paperRecord.setTotalAuthors(this.totalAuthors);
        paperRecord.setUnitIsFirstUnit(this.unitIsFirstUnit);
        paperRecord.setBaseScore(this.baseScore);
        paperRecord.setComputedScore(this.computedScore);
        
        // 处理证据文件
        if (this.evidenceFiles != null && !this.evidenceFiles.isEmpty()) {
            paperRecord.setEvidenceFiles(String.join(",", this.evidenceFiles));
        } else {
            paperRecord.setEvidenceFiles("");
        }
        
        return paperRecord;
    }
    
    // Getter和Setter方法
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
    
    public List<String> getEvidenceFiles() {
        return evidenceFiles;
    }
    
    public void setEvidenceFiles(List<String> evidenceFiles) {
        this.evidenceFiles = evidenceFiles;
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
}