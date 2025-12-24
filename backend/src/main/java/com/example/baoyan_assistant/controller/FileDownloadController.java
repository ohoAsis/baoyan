package com.example.baoyan_assistant.controller;

import com.example.baoyan_assistant.entity.FileRecord;
import com.example.baoyan_assistant.service.FileAccessService;
import com.example.baoyan_assistant.util.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 文件下载控制器
 * 提供安全的文件访问机制
 */
@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*")
public class FileDownloadController {

    private static final Logger logger = LoggerFactory.getLogger(FileDownloadController.class);

    @Autowired
    private FileAccessService fileAccessService;

    /**
     * 获取文件（支持预览和下载）
     * GET /api/files/{fileId}
     * @param fileId 文件ID
     * @return 文件流
     */
    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> getFile(@PathVariable Long fileId) {
        
        // 从UserContext获取用户信息
        Long userId = UserContext.getUserId();
        String userRole = UserContext.getUserRole();
        
        // 验证用户权限
        boolean isReviewer = "reviewer".equals(userRole) || "admin".equals(userRole);
        boolean canAccess = fileAccessService.canAccessFile(userId, fileId, isReviewer);
        
        if (!canAccess) {
            logger.warn("用户无权限访问文件: userId={}, fileId={}, userRole={}", userId, fileId, userRole);
            return ResponseEntity.status(403).body(null);
        }
        
        // 获取文件记录
        FileRecord fileRecord = fileAccessService.getFileRecord(fileId);
        
        // 记录访问日志
        logger.info("用户访问文件: userId={}, fileId={}, fileName={}, userRole={}", 
                userId, fileId, fileRecord.getOriginalFileName(), userRole);
        
        // 读取文件
        File file = new File(fileRecord.getRealPath());
        if (!file.exists()) {
            logger.warn("文件不存在: fileId={}, realPath={}", fileId, fileRecord.getRealPath());
            return ResponseEntity.notFound().build();
        }
        
        // 创建文件资源
        Resource resource = new FileSystemResource(file);
        
        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(fileRecord.getFileType()));
        
        // 设置Content-Disposition，支持中文文件名
        String encodedFileName = URLEncoder.encode(fileRecord.getOriginalFileName(), StandardCharsets.UTF_8);
        
        // 决定是预览还是下载
        String fileType = fileRecord.getFileType();
        if (isPreviewable(fileType)) {
            // 预览：PDF和图片类型
            headers.setContentDispositionFormData("inline", encodedFileName);
        } else {
            // 下载：其他类型
            headers.setContentDispositionFormData("attachment", encodedFileName);
        }
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
    
    /**
     * 判断文件类型是否支持预览
     * @param fileType 文件MIME类型
     * @return true if previewable, false otherwise
     */
    private boolean isPreviewable(String fileType) {
        if (fileType == null) {
            return false;
        }
        
        // 支持预览的文件类型：PDF和图片
        return fileType.startsWith("image/") || "application/pdf".equals(fileType);
    }
}