package com.example.baoyan_assistant.dto;

import com.example.baoyan_assistant.entity.CompetitionRecord;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 竞赛记录DTO类
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompetitionRecordDTO {
    
    private Long id;
    private String studentId;
    private String name;
    private String level;
    private String award;
    private Boolean isTeamProject;
    private Integer teamRank;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate awardDate;
    
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
    public CompetitionRecordDTO() {
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
     * @param entity 竞赛记录实体
     * @return 竞赛记录DTO
     */
    public static CompetitionRecordDTO fromEntity(CompetitionRecord entity) {
        if (entity == null) {
            return null;
        }

        CompetitionRecordDTO dto = new CompetitionRecordDTO();
        dto.setId(entity.getId());
        dto.setStudentId(entity.getStudent().getStudentId());
        dto.setName(entity.getName());
        dto.setLevel(entity.getLevel());
        dto.setAward(entity.getAward());
        dto.setIsTeamProject(entity.getIsTeamProject());
        dto.setTeamRank(entity.getTeamRank());
        dto.setAwardDate(entity.getAwardDate());
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
     * @param dto 竞赛记录DTO
     * @return 竞赛记录实体
     */
    public static CompetitionRecord toEntity(CompetitionRecordDTO dto) {
        if (dto == null) {
            return null;
        }

        CompetitionRecord entity = new CompetitionRecord();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setLevel(dto.getLevel());
        entity.setAward(dto.getAward());
        entity.setIsTeamProject(dto.getIsTeamProject());
        entity.setTeamRank(dto.getTeamRank());
        entity.setAwardDate(dto.getAwardDate());
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