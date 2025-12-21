package com.example.baoyan_assistant.service;

import com.example.baoyan_assistant.entity.FileRecord;
import com.example.baoyan_assistant.repository.ApplicationRepository;
import com.example.baoyan_assistant.repository.FileRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

/**
 * 文件清理服务
 * 负责定期清理无用文件和数据库记录
 */
@Service
@Transactional
public class FileCleanupService {

    private static final Logger logger = LoggerFactory.getLogger(FileCleanupService.class);

    @Autowired
    private FileRecordRepository fileRecordRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    /**
     * 统一清理方法，依次调用所有清理逻辑
     */
    public void cleanAll() {
        logger.info("开始执行文件清理任务");
        
        cleanDeletedFiles();
        cleanOrphanFiles();
        cleanMissingPhysicalFiles();
        
        logger.info("文件清理任务执行完成");
    }

    /**
     * 清理已标记为删除的文件
     * - 查询 deleted = true 的 FileRecord
     * - 删除对应的物理文件
     * - 永久删除 FileRecord 记录
     */
    public void cleanDeletedFiles() {
        logger.info("开始清理已标记为删除的文件");
        
        List<FileRecord> deletedFiles = fileRecordRepository.findAllByDeletedTrue();
        logger.info("找到 {} 个已标记为删除的文件记录", deletedFiles.size());
        
        for (FileRecord fileRecord : deletedFiles) {
            String fileName = fileRecord.getOriginalFileName();
            String realPath = fileRecord.getRealPath();
            Long applicationId = fileRecord.getApplicationId();
            Long uploaderId = fileRecord.getUploaderId();
            Long fileId = fileRecord.getId();
            
            try {
                // 尝试删除物理文件
                File physicalFile = new File(realPath);
                if (physicalFile.exists()) {
                    if (physicalFile.delete()) {
                        logger.info("成功删除物理文件: {}, 路径: {}", fileName, realPath);
                    } else {
                        logger.warn("无法删除物理文件: {}, 路径: {}", fileName, realPath);
                    }
                } else {
                    logger.warn("物理文件不存在，跳过删除: {}, 路径: {}", fileName, realPath);
                }
                
                // 永久删除数据库记录
                fileRecordRepository.delete(fileRecord);
                logger.info("成功删除已标记为删除的文件记录: ID={}, 文件名={}, applicationId={}, uploaderId={}", 
                        fileId, fileName, applicationId, uploaderId);
                
            } catch (Exception e) {
                logger.error("清理已删除文件时发生错误: ID={}, 文件名={}, 错误: {}", 
                        fileId, fileName, e.getMessage(), e);
                // 继续处理其他文件
            }
        }
        
        logger.info("已标记为删除的文件清理完成");
    }

    /**
     * 清理孤立文件
     * - 查找 applicationId 为空或关联的 Application 不存在的 FileRecord
     * - 删除对应的物理文件
     * - 永久删除 FileRecord 记录
     */
    public void cleanOrphanFiles() {
        logger.info("开始清理孤立文件");
        
        List<FileRecord> allFiles = fileRecordRepository.findAll();
        int orphanCount = 0;
        
        for (FileRecord fileRecord : allFiles) {
            Long applicationId = fileRecord.getApplicationId();
            boolean isOrphan = false;
            
            if (applicationId == null) {
                isOrphan = true;
            } else {
                // 检查关联的 Application 是否存在
                isOrphan = !applicationRepository.existsById(applicationId);
            }
            
            if (isOrphan) {
                orphanCount++;
                String fileName = fileRecord.getOriginalFileName();
                String realPath = fileRecord.getRealPath();
                Long uploaderId = fileRecord.getUploaderId();
                Long fileId = fileRecord.getId();
                
                try {
                    // 尝试删除物理文件
                    File physicalFile = new File(realPath);
                    if (physicalFile.exists()) {
                        if (physicalFile.delete()) {
                            logger.info("成功删除孤立文件的物理文件: {}, 路径: {}", fileName, realPath);
                        } else {
                            logger.warn("无法删除孤立文件的物理文件: {}, 路径: {}", fileName, realPath);
                        }
                    } else {
                        logger.warn("孤立文件的物理文件不存在，跳过删除: {}, 路径: {}", fileName, realPath);
                    }
                    
                    // 永久删除数据库记录
                    fileRecordRepository.delete(fileRecord);
                    logger.info("成功删除孤立文件记录: ID={}, 文件名={}, applicationId={}, uploaderId={}", 
                            fileId, fileName, applicationId, uploaderId);
                    
                } catch (Exception e) {
                    logger.error("清理孤立文件时发生错误: ID={}, 文件名={}, 错误: {}", 
                            fileId, fileName, e.getMessage(), e);
                    // 继续处理其他文件
                }
            }
        }
        
        logger.info("孤立文件清理完成，共清理 {} 个孤立文件", orphanCount);
    }

    /**
     * 清理数据库存在但物理文件不存在的记录
     * - 检查每个 FileRecord 的 realPath 对应的文件是否存在
     * - 如果不存在，删除数据库记录
     */
    public void cleanMissingPhysicalFiles() {
        logger.info("开始清理数据库存在但物理文件不存在的记录");
        
        List<FileRecord> allFiles = fileRecordRepository.findAll();
        int missingCount = 0;
        
        for (FileRecord fileRecord : allFiles) {
            String fileName = fileRecord.getOriginalFileName();
            String realPath = fileRecord.getRealPath();
            Long applicationId = fileRecord.getApplicationId();
            Long uploaderId = fileRecord.getUploaderId();
            Long fileId = fileRecord.getId();
            
            try {
                File physicalFile = new File(realPath);
                if (!physicalFile.exists()) {
                    missingCount++;
                    logger.warn("发现数据库记录存在但物理文件不存在: ID={}, 文件名={}, 路径={}, applicationId={}, uploaderId={}", 
                            fileId, fileName, realPath, applicationId, uploaderId);
                    
                    // 删除数据库记录
                    fileRecordRepository.delete(fileRecord);
                    logger.info("已删除缺失物理文件的数据库记录: ID={}, 文件名={}", fileId, fileName);
                }
                
            } catch (Exception e) {
                logger.error("清理缺失物理文件的记录时发生错误: ID={}, 文件名={}, 错误: {}", 
                        fileId, fileName, e.getMessage(), e);
                // 继续处理其他文件
            }
        }
        
        logger.info("缺失物理文件记录清理完成，共清理 {} 个记录", missingCount);
    }
}