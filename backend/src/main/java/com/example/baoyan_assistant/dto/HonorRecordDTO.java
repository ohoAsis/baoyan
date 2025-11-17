package com.example.baoyan_assistant.dto;

import com.example.baoyan_assistant.entity.HonorRecord;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 荣誉记录DTO类
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HonorRecordDTO {
    
    private Long id;
    private String title;
    private String level;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    
    private Double baseScore;
    private Double computedScore;
    private List<String> evidenceFiles;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updateTime;

    // ==================== 构造函数 ====================

    /**
     * 无参构造函数
     */
    public HonorRecordDTO() {
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    // ==================== 实体转换方法 ====================

    /**
     * 从实体转换为DTO
     * @param entity 荣誉记录实体
     * @return 荣誉记录DTO
     */
    public static HonorRecordDTO fromEntity(HonorRecord entity) {
        if (entity == null) {
            return null;
        }

        HonorRecordDTO dto = new HonorRecordDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setLevel(entity.getLevel());
        dto.setDate(entity.getDate());
        dto.setBaseScore(entity.getBaseScore());
        dto.setComputedScore(entity.getComputedScore());
        
        // 处理证据文件字符串转换为列表
        if (entity.getEvidenceFiles() != null && !entity.getEvidenceFiles().isEmpty()) {
            List<String> files = Arrays.asList(entity.getEvidenceFiles().split(","));
            dto.setEvidenceFiles(files);
        }
        
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        
        return dto;
    }

    /**
     * 从DTO转换为实体
     * @param dto 荣誉记录DTO
     * @return 荣誉记录实体
     */
    public static HonorRecord toEntity(HonorRecordDTO dto) {
        if (dto == null) {
            return null;
        }

        HonorRecord entity = new HonorRecord();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setLevel(dto.getLevel());
        entity.setDate(dto.getDate());
        entity.setBaseScore(dto.getBaseScore());
        entity.setComputedScore(dto.getComputedScore());
        
        // 处理证据文件列表转换为字符串
        if (dto.getEvidenceFiles() != null && !dto.getEvidenceFiles().isEmpty()) {
            String files = String.join(",", dto.getEvidenceFiles());
            entity.setEvidenceFiles(files);
        }
        
        return entity;
    }
}