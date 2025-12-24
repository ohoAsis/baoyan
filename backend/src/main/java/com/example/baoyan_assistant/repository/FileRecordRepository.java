package com.example.baoyan_assistant.repository;

import com.example.baoyan_assistant.entity.FileRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * FileRecord的JPA Repository
 */
@Repository
public interface FileRecordRepository extends JpaRepository<FileRecord, Long> {

    /**
     * 根据applicationId查询未删除的文件记录
     * @param applicationId 申请ID
     * @return 文件记录列表
     */
    List<FileRecord> findByApplicationIdAndDeletedFalse(Long applicationId);

    /**
     * 根据uploaderId查询未删除的文件记录
     * @param uploaderId 上传人ID
     * @return 文件记录列表
     */
    List<FileRecord> findByUploaderIdAndDeletedFalse(Long uploaderId);

    /**
     * 根据storedFileName查询未删除的文件记录
     * @param storedFileName 存储文件名
     * @return 文件记录
     */
    FileRecord findByStoredFileNameAndDeletedFalse(String storedFileName);

    /**
     * 根据applicationId查询所有文件记录（包括已删除）
     * @param applicationId 申请ID
     * @return 文件记录列表
     */
    List<FileRecord> findByApplicationId(Long applicationId);

    /**
     * 查询所有已删除的文件记录
     * @return 已删除的文件记录列表
     */
    List<FileRecord> findAllByDeletedTrue();
}