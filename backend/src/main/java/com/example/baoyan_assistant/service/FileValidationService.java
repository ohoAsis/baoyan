package com.example.baoyan_assistant.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 文件验证服务
 * 提供文件上传的安全验证机制
 */
@Service
public class FileValidationService {

    private static final Logger logger = LoggerFactory.getLogger(FileValidationService.class);

    // 允许的文件扩展名白名单
    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(Arrays.asList(
            "pdf", "jpg", "jpeg", "png", "doc", "docx"
    ));

    // 允许的MIME类型白名单
    private static final Set<String> ALLOWED_MIME_TYPES = new HashSet<>(Arrays.asList(
            "application/pdf",
            "image/jpeg",
            "image/png",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    ));

    // 禁止的可执行文件扩展名
    private static final Set<String> FORBIDDEN_EXECUTABLE_EXTENSIONS = new HashSet<>(Arrays.asList(
            "exe", "sh", "bat", "msi", "jar", "war", "dll", "py", "php", "jsp", "asp", "aspx", "pl"
    ));

    // 文件大小限制，默认10MB
    @Value("${file.max-size:10485760}")
    private long maxFileSize;

    /**
     * 验证文件扩展名
     * @param filename 文件名
     * @return true if valid, false otherwise
     */
    public boolean validateExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            logger.warn("文件名不能为空");
            return false;
        }

        // 获取文件扩展名
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            logger.warn("文件没有扩展名: {}", filename);
            return false;
        }

        String extension = filename.substring(lastDotIndex + 1).toLowerCase();

        // 检查是否为可执行文件
        if (FORBIDDEN_EXECUTABLE_EXTENSIONS.contains(extension)) {
            logger.warn("禁止上传可执行文件: {}, 扩展名: {}", filename, extension);
            return false;
        }

        // 检查是否在允许的扩展名列表中
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            logger.warn("文件扩展名不允许: {}, 扩展名: {}", filename, extension);
            return false;
        }

        return true;
    }

    /**
     * 验证文件MIME类型
     * @param file MultipartFile
     * @return true if valid, false otherwise
     */
    public boolean validateMimeType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null) {
            logger.warn("无法获取文件MIME类型: {}", file.getOriginalFilename());
            return false;
        }

        if (!ALLOWED_MIME_TYPES.contains(contentType)) {
            logger.warn("文件MIME类型不允许: {}, MIME类型: {}", file.getOriginalFilename(), contentType);
            return false;
        }

        return true;
    }

    /**
     * 验证文件大小
     * @param file MultipartFile
     * @return true if valid, false otherwise
     */
    public boolean validateFileSize(MultipartFile file) {
        if (file.getSize() > maxFileSize) {
            logger.warn("文件大小超过限制: {}, 大小: {} bytes, 限制: {} bytes", 
                    file.getOriginalFilename(), file.getSize(), maxFileSize);
            return false;
        }
        return true;
    }

    /**
     * 清理文件名，防止路径穿越攻击
     * @param originalFilename 原始文件名
     * @return 清理后的文件名
     */
    public String sanitizeFileName(String originalFilename) {
        if (originalFilename == null) {
            return "unknown";
        }

        // 直接获取文件名，忽略任何路径信息
        String filename = new File(originalFilename).getName();

        // 移除所有特殊字符，只保留字母、数字、下划线、点和中文字符
        String sanitized = filename.replaceAll("[^一-龥a-zA-Z0-9._-]", "");

        // 如果清理后为空，返回默认名
        if (sanitized.isEmpty() || sanitized.equals(".")) {
            return "unknown";
        }

        return sanitized;
    }

    /**
     * 检查文件是否为可执行文件
     * @param filename 文件名
     * @return true if executable, false otherwise
     */
    public boolean isExecutable(String filename) {
        if (filename == null || filename.isEmpty()) {
            return false;
        }

        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return false;
        }

        String extension = filename.substring(lastDotIndex + 1).toLowerCase();
        return FORBIDDEN_EXECUTABLE_EXTENSIONS.contains(extension);
    }

    /**
     * 生成唯一的存储文件名（UUID + 原始扩展名）
     * @param originalFilename 原始文件名
     * @return 唯一存储文件名
     */
    public String generateUniqueStoredFileName(String originalFilename) {
        String sanitized = sanitizeFileName(originalFilename);
        String extension = "";

        // 提取原始扩展名
        int lastDotIndex = sanitized.lastIndexOf('.');
        if (lastDotIndex != -1 && lastDotIndex < sanitized.length() - 1) {
            extension = sanitized.substring(lastDotIndex + 1).toLowerCase();
        }

        // 生成UUID
        String uuid = UUID.randomUUID().toString().replace("-", "");

        // 返回 UUID + 扩展名
        return extension.isEmpty() ? uuid : uuid + "." + extension;
    }

    /**
     * 统一验证所有规则
     * @param file MultipartFile
     * @return 验证结果，null表示验证通过，否则为错误信息
     */
    public String validateAll(MultipartFile file) {
        // 验证文件是否为空
        if (file.isEmpty()) {
            String error = "文件不能为空";
            logger.warn(error);
            return error;
        }

        // 获取原始文件名并清理
        String originalFilename = file.getOriginalFilename();
        String sanitizedFilename = sanitizeFileName(originalFilename);

        // 验证文件大小
        if (!validateFileSize(file)) {
            return "文件大小超过限制，最大允许 " + (maxFileSize / 1024 / 1024) + "MB";
        }

        // 验证文件扩展名
        if (!validateExtension(sanitizedFilename)) {
            return "文件类型不允许";
        }

        // 验证MIME类型
        if (!validateMimeType(file)) {
            return "文件MIME类型不允许";
        }

        // 再次检查是否为可执行文件
        if (isExecutable(sanitizedFilename)) {
            return "禁止上传可执行文件";
        }

        return null; // 验证通过
    }
}