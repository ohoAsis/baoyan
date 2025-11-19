package com.example.baoyan_assistant.dto;

import com.example.baoyan_assistant.entity.PatentRecord;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 专利记录DTO类
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatentRecordDTO {
    
    private Long id;
    private String title;
    private String type;
    private String studentId;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate authorizationDate;
    
    private Boolean isFirstAuthor;
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
    public PatentRecordDTO() {
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

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
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
     * @param entity 专利记录实体
     * @return 专利记录DTO
     */
    public static PatentRecordDTO fromEntity(PatentRecord entity) {
        if (entity == null) {
            return null;
        }

        PatentRecordDTO dto = new PatentRecordDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setType(entity.getType());
        
        // 设置学生ID
        if (entity.getStudent() != null) {
            dto.setStudentId(entity.getStudent().getStudentId());
        }
        
        dto.setAuthorizationDate(entity.getAuthorizationDate());
        dto.setIsFirstAuthor(entity.getIsFirstAuthor());
        dto.setBaseScore(entity.getBaseScore());
        dto.setComputedScore(entity.getComputedScore());
        
        // 处理证据文件字符串转换为列表
        if (entity.getEvidenceFiles() != null && !entity.getEvidenceFiles().isEmpty()) {
            // 使用Arrays.stream(...).toList()方法转换
            List<String> files = Arrays.stream(entity.getEvidenceFiles().split(",")).toList();
            dto.setEvidenceFiles(files);
        }
        
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        
        return dto;
    }

    /**
     * 从DTO转换为实体
     * @param dto 专利记录DTO
     * @return 专利记录实体
     */
    public static PatentRecord toEntity(PatentRecordDTO dto) {
        if (dto == null) {
            return null;
        }

        PatentRecord entity = new PatentRecord();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setType(dto.getType());
        entity.setAuthorizationDate(dto.getAuthorizationDate());
        entity.setIsFirstAuthor(dto.getIsFirstAuthor());
        entity.setBaseScore(dto.getBaseScore());
        entity.setComputedScore(dto.getComputedScore());
        
        // 处理证据文件列表转换为字符串
        if (dto.getEvidenceFiles() != null && !dto.getEvidenceFiles().isEmpty()) {
            String files = String.join(",", dto.getEvidenceFiles());
            entity.setEvidenceFiles(files);
        } else {
            entity.setEvidenceFiles("");
        }
        
        return entity;
    }
}