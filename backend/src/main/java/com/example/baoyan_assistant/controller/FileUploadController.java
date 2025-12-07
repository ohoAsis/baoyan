package com.example.baoyan_assistant.controller;

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
    
    @Value("${file.upload.dir:uploads}")
    private String uploadDir;
    
    // 允许的文件类型
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
        "pdf", "jpg", "jpeg", "png", "gif", "doc", "docx"
    );
    
    // 最大文件大小（10MB）
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
    
    /**
     * 上传单个文件
     * POST /api/upload
     * @param file 上传的文件
     * @return 文件路径
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 验证文件
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("message", "文件不能为空");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 验证文件大小
            if (file.getSize() > MAX_FILE_SIZE) {
                response.put("success", false);
                response.put("message", "文件大小不能超过10MB");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 验证文件类型
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                response.put("success", false);
                response.put("message", "文件名不能为空");
                return ResponseEntity.badRequest().body(response);
            }
            
            String extension = getFileExtension(originalFilename).toLowerCase();
            if (!ALLOWED_EXTENSIONS.contains(extension)) {
                response.put("success", false);
                response.put("message", "不支持的文件类型，仅支持：PDF、JPG、PNG、DOC、DOCX");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 创建上传目录
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // 生成唯一文件名
            String uniqueFilename = generateUniqueFilename(originalFilename);
            Path filePath = uploadPath.resolve(uniqueFilename);
            
            // 保存文件
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // 返回文件路径（相对于上传目录）
            String fileUrl = "/" + uploadDir + "/" + uniqueFilename;
            
            response.put("success", true);
            response.put("message", "文件上传成功");
            response.put("fileUrl", fileUrl);
            response.put("fileName", uniqueFilename);
            response.put("originalFileName", originalFilename);
            response.put("fileSize", file.getSize());
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "文件上传失败：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 批量上传文件
     * POST /api/upload/multiple
     * @param files 上传的文件列表
     * @return 文件路径列表
     */
    @PostMapping("/multiple")
    public ResponseEntity<Map<String, Object>> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> uploadedFiles = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        
        try {
            for (MultipartFile file : files) {
                Map<String, Object> fileResponse = new HashMap<>();
                
                try {
                    // 验证文件
                    if (file.isEmpty()) {
                        errors.add(file.getOriginalFilename() + ": 文件不能为空");
                        continue;
                    }
                    
                    // 验证文件大小
                    if (file.getSize() > MAX_FILE_SIZE) {
                        errors.add(file.getOriginalFilename() + ": 文件大小不能超过10MB");
                        continue;
                    }
                    
                    // 验证文件类型
                    String originalFilename = file.getOriginalFilename();
                    if (originalFilename == null) {
                        errors.add("文件名不能为空");
                        continue;
                    }
                    
                    String extension = getFileExtension(originalFilename).toLowerCase();
                    if (!ALLOWED_EXTENSIONS.contains(extension)) {
                        errors.add(originalFilename + ": 不支持的文件类型");
                        continue;
                    }
                    
                    // 创建上传目录
                    Path uploadPath = Paths.get(uploadDir);
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }
                    
                    // 生成唯一文件名
                    String uniqueFilename = generateUniqueFilename(originalFilename);
                    Path filePath = uploadPath.resolve(uniqueFilename);
                    
                    // 保存文件
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    
                    // 返回文件路径
                    String fileUrl = "/" + uploadDir + "/" + uniqueFilename;
                    
                    Map<String, Object> fileInfo = new HashMap<>();
                    fileInfo.put("fileUrl", fileUrl);
                    fileInfo.put("fileName", uniqueFilename);
                    fileInfo.put("originalFileName", originalFilename);
                    fileInfo.put("fileSize", file.getSize());
                    uploadedFiles.add(fileInfo);
                    
                } catch (IOException e) {
                    errors.add(file.getOriginalFilename() + ": " + e.getMessage());
                }
            }
            
            response.put("success", uploadedFiles.size() > 0);
            response.put("files", uploadedFiles);
            if (!errors.isEmpty()) {
                response.put("errors", errors);
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "批量上传失败：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1);
    }
    
    /**
     * 生成唯一文件名
     */
    private String generateUniqueFilename(String originalFilename) {
        String extension = getFileExtension(originalFilename);
        String nameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = UUID.randomUUID().toString().substring(0, 8);
        return nameWithoutExtension + "_" + timestamp + "_" + random + "." + extension;
    }
}

