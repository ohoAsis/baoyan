package com.example.baoyan_assistant.service;

import com.example.baoyan_assistant.entity.FileRecord;
import com.example.baoyan_assistant.repository.FileRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * FileRecord服务类
 * 提供文件记录的业务逻辑处理
 */
@Service
@Transactional
public class FileRecordService {

    @Autowired
    private FileRecordRepository fileRecordRepository;

    /**
     * 保存文件记录
     * @param fileRecord 文件记录
     * @return 保存后的文件记录
     */
    public FileRecord save(FileRecord fileRecord) {
        // 设置上传时间（如果未设置）
        if (fileRecord.getUploadTime() == null) {
            fileRecord.setUploadTime(LocalDateTime.now());
        }
        // 设置软删除标记（默认为false）
        if (fileRecord.getDeleted() == null) {
            fileRecord.setDeleted(false);
        }
        return fileRecordRepository.save(fileRecord);
    }

    /**
     * 批量保存文件记录
     * @param fileRecords 文件记录列表
     * @return 保存后的文件记录列表
     */
    public List<FileRecord> saveAll(List<FileRecord> fileRecords) {
        // 设置默认值
        fileRecords.forEach(record -> {
            if (record.getUploadTime() == null) {
                record.setUploadTime(LocalDateTime.now());
            }
            if (record.getDeleted() == null) {
                record.setDeleted(false);
            }
        });
        return fileRecordRepository.saveAll(fileRecords);
    }

    /**
     * 根据ID查询文件记录
     * @param id 文件记录ID
     * @return 文件记录
     * @throws RuntimeException 如果文件记录不存在或已删除
     */
    @Transactional(readOnly = true)
    public FileRecord getById(Long id) {
        FileRecord fileRecord = fileRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("文件记录不存在，ID: " + id));
        
        if (fileRecord.getDeleted()) {
            throw new RuntimeException("文件记录已删除，ID: " + id);
        }
        
        return fileRecord;
    }

    /**
     * 根据applicationId查询未删除的文件记录列表
     * @param applicationId 申请ID
     * @return 文件记录列表
     */
    @Transactional(readOnly = true)
    public List<FileRecord> getByApplicationId(Long applicationId) {
        return fileRecordRepository.findByApplicationIdAndDeletedFalse(applicationId);
    }

    /**
     * 根据uploaderId查询未删除的文件记录列表
     * @param uploaderId 上传人ID
     * @return 文件记录列表
     */
    @Transactional(readOnly = true)
    public List<FileRecord> getByUploaderId(Long uploaderId) {
        return fileRecordRepository.findByUploaderIdAndDeletedFalse(uploaderId);
    }

    /**
     * 根据ID列表查询文件记录
     * @param ids 文件记录ID列表
     * @return 文件记录列表
     */
    @Transactional(readOnly = true)
    public List<FileRecord> findByIds(List<Long> ids) {
        return fileRecordRepository.findAllById(ids);
    }

    /**
     * 软删除文件记录
     * @param id 文件记录ID
     * @return 删除后的文件记录
     * @throws RuntimeException 如果文件记录不存在
     */
    public FileRecord softDelete(Long id) {
        FileRecord fileRecord = fileRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("文件记录不存在，ID: " + id));
        
        fileRecord.setDeleted(true);
        return fileRecordRepository.save(fileRecord);
    }

    /**
     * 批量软删除文件记录
     * @param ids 文件记录ID列表
     * @return 删除的文件记录数量
     */
    public int softDeleteBatch(List<Long> ids) {
        int deletedCount = 0;
        for (Long id : ids) {
            try {
                softDelete(id);
                deletedCount++;
            } catch (RuntimeException e) {
                // 忽略不存在的记录，继续处理其他记录
            }
        }
        return deletedCount;
    }

    /**
     * 根据applicationId软删除所有关联文件记录
     * @param applicationId 申请ID
     * @return 删除的文件记录数量
     */
    public int softDeleteByApplicationId(Long applicationId) {
        List<FileRecord> fileRecords = fileRecordRepository.findByApplicationId(applicationId);
        int deletedCount = 0;
        for (FileRecord fileRecord : fileRecords) {
            if (!fileRecord.getDeleted()) {
                fileRecord.setDeleted(true);
                fileRecordRepository.save(fileRecord);
                deletedCount++;
            }
        }
        return deletedCount;
    }
}