package com.example.baoyan_assistant.service;

import com.example.baoyan_assistant.entity.Application;
import com.example.baoyan_assistant.entity.FileRecord;
import com.example.baoyan_assistant.repository.ApplicationRepository;
import com.example.baoyan_assistant.repository.FileRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 文件访问权限服务
 * 负责验证用户是否有权限访问特定文件
 */
@Service
public class FileAccessService {

    private static final Logger logger = LoggerFactory.getLogger(FileAccessService.class);

    @Autowired
    private FileRecordRepository fileRecordRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    /**
     * 检查用户是否有权限访问文件
     * @param userId 当前用户ID
     * @param fileId 要访问的文件ID
     * @param isReviewer 当前用户是否为审核员
     * @return true if authorized, false otherwise
     */
    public boolean canAccessFile(Long userId, Long fileId, boolean isReviewer) {
        // 查询文件记录
        FileRecord fileRecord = fileRecordRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("文件不存在，ID: " + fileId));

        // 如果是学生角色
        if (!isReviewer) {
            // 学生只能访问自己上传的文件
            return fileRecord.getUploaderId().equals(userId);
        }
        
        // 如果是审核员角色
        // 可以访问与被审核Application相关的文件
        Long applicationId = fileRecord.getApplicationId();
        if (applicationId == null) {
            // 如果文件没有关联Application，审核员也不能访问
            return false;
        }
        
        // 检查关联的Application是否存在
        Application application = applicationRepository.findById(applicationId)
                .orElse(null);
        
        // 审核员可以访问所有与Application关联的文件
        return application != null;
    }

    /**
     * 根据fileId获取文件记录
     * @param fileId 文件ID
     * @return FileRecord
     */
    public FileRecord getFileRecord(Long fileId) {
        return fileRecordRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("文件不存在，ID: " + fileId));
    }
}
