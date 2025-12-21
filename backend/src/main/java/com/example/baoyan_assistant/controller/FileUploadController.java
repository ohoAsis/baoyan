package com.example.baoyan_assistant.controller;

import com.example.baoyan_assistant.service.FileValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

/**
 * 文件上传控制器
 * 提供文件上传的 RESTful API 接口
 */
@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "*")
public class FileUploadController {
    
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);
    
    @Autowired
    private FileValidationService fileValidationService;
    
    @Value("${file.upload.dir:uploads}")
    private String uploadDir;
    
    /**
     * 上传单个文件
     * POST /api/upload
     * @param file 上传的文件
     * @return 文件路径
     */
    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // 统一验证所有规则
            String validationError = fileValidationService.validateAll(file);
            if (validationError != null) {
                return ResponseEntity.badRequest().body(Map.of("error", validationError));
            }
            
            // 创建上传目录
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // 生成唯一文件名（UUID + 扩展名）
            String storedFileName = fileValidationService.generateUniqueStoredFileName(file.getOriginalFilename());
            Path filePath = uploadPath.resolve(storedFileName);
            
            // 保存文件
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // 返回文件路径（相对于上传目录）
            String fileUrl = "/" + uploadDir + "/" + storedFileName;
            // 服务器真实路径
            String realPath = filePath.toAbsolutePath().toString();
            // 获取文件MIME类型
            String fileType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";
            
            Map<String, Object> response = new HashMap<>();
            response.put("fileUrl", fileUrl);
            response.put("storedFileName", storedFileName);
            response.put("realPath", realPath);
            response.put("originalFileName", file.getOriginalFilename());
            response.put("fileSize", file.getSize());
            response.put("fileType", fileType);
            
            logger.info("文件上传成功: {}, 存储为: {}, 大小: {} bytes, MIME类型: {}",
                    file.getOriginalFilename(), storedFileName, file.getSize(), fileType);
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            String errorMessage = "文件上传失败：" + e.getMessage();
            logger.error(errorMessage, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", errorMessage));
        } catch (Exception e) {
            String errorMessage = "文件上传失败：" + e.getMessage();
            logger.error(errorMessage, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", errorMessage));
        }
    }
    
    /**
     * 批量上传文件
     * POST /api/upload/multiple
     * @param files 上传的文件列表
     * @return 文件路径列表
     */
    @PostMapping("/multiple")
    public ResponseEntity<?> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        try {
            List<Map<String, Object>> uploadedFiles = new ArrayList<>();
            List<String> errors = new ArrayList<>();
            
            // 创建上传目录
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            for (MultipartFile file : files) {
                try {
                    // 统一验证所有规则
                    String validationError = fileValidationService.validateAll(file);
                    if (validationError != null) {
                        errors.add((file.getOriginalFilename() != null ? file.getOriginalFilename() : "未知文件") + ": " + validationError);
                        continue;
                    }
                    
                    // 生成唯一文件名（UUID + 扩展名）
                    String storedFileName = fileValidationService.generateUniqueStoredFileName(file.getOriginalFilename());
                    Path filePath = uploadPath.resolve(storedFileName);
                    
                    // 保存文件
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    
                    // 返回文件路径
                    String fileUrl = "/" + uploadDir + "/" + storedFileName;
                    // 服务器真实路径
                    String realPath = filePath.toAbsolutePath().toString();
                    // 获取文件MIME类型
                    String fileType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";
                    
                    Map<String, Object> fileInfo = new HashMap<>();
                    fileInfo.put("fileUrl", fileUrl);
                    fileInfo.put("storedFileName", storedFileName);
                    fileInfo.put("realPath", realPath);
                    fileInfo.put("originalFileName", file.getOriginalFilename());
                    fileInfo.put("fileSize", file.getSize());
                    fileInfo.put("fileType", fileType);
                    uploadedFiles.add(fileInfo);
                    
                    logger.info("文件上传成功: {}, 存储为: {}, 大小: {} bytes, MIME类型: {}",
                            file.getOriginalFilename(), storedFileName, file.getSize(), fileType);
                    
                } catch (IOException e) {
                    String errorMsg = (file.getOriginalFilename() != null ? file.getOriginalFilename() : "未知文件") + ": " + e.getMessage();
                    errors.add(errorMsg);
                    logger.error(errorMsg, e);
                } catch (Exception e) {
                    String errorMsg = (file.getOriginalFilename() != null ? file.getOriginalFilename() : "未知文件") + ": " + e.getMessage();
                    errors.add(errorMsg);
                    logger.error(errorMsg, e);
                }
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("files", uploadedFiles);
            if (!errors.isEmpty()) {
                response.put("errors", errors);
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            String errorMessage = "批量上传失败：" + e.getMessage();
            logger.error(errorMessage, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", errorMessage));
        }
    }
    
}

