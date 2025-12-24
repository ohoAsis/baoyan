package com.example.baoyan_assistant.dto;

import com.example.baoyan_assistant.entity.FileRecord;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件记录DTO
 * 用于将FileRecord实体转换为符合前端要求的JSON格式
 */
public class FileRecordDTO {
    
    private Long id;
    private String originalFileName;
    private String fileType;
    private Long fileSize;
    
    // 默认构造函数
    public FileRecordDTO() {
    }
    
    // 从实体转换为DTO的静态方法
    public static FileRecordDTO fromEntity(FileRecord fileRecord) {
        FileRecordDTO dto = new FileRecordDTO();
        dto.setId(fileRecord.getId());
        dto.setOriginalFileName(fileRecord.getOriginalFileName());
        dto.setFileType(fileRecord.getFileType());
        dto.setFileSize(fileRecord.getFileSize());
        return dto;
    }
    
    // 批量转换方法
    public static List<FileRecordDTO> fromEntities(List<FileRecord> fileRecords) {
        if (fileRecords == null || fileRecords.isEmpty()) {
            return List.of();
        }
        return fileRecords.stream()
                .map(FileRecordDTO::fromEntity)
                .collect(Collectors.toList());
    }
    
    // Getter和Setter方法
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getOriginalFileName() {
        return originalFileName;
    }
    
    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }
    
    public String getFileType() {
        return fileType;
    }
    
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    
    public Long getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
}