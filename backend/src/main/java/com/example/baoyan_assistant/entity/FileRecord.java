package com.example.baoyan_assistant.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 文件记录实体类
 * 用于存储文件的元数据和关联信息
 */
@Entity
@Table(name = "file_record")
public class FileRecord {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 前端访问路径，例如 /uploads/...
     */
    @Column(name = "file_url", nullable = false, length = 255)
    private String fileUrl;

    /**
     * 服务器存储路径，例如 /app/uploads/...
     */
    @Column(name = "real_path", nullable = false, length = 255)
    private String realPath;

    /**
     * 用户上传的原始文件名
     */
    @Column(name = "original_file_name", nullable = false, length = 255)
    private String originalFileName;

    /**
     * 服务器为文件生成的唯一文件名（UUID）
     */
    @Column(name = "stored_file_name", nullable = false, length = 255)
    private String storedFileName;

    /**
     * 文件大小（字节）
     */
    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    /**
     * MIME类型，例如 application/pdf
     */
    @Column(name = "file_type", nullable = false, length = 100)
    private String fileType;

    /**
     * 上传时间
     */
    @Column(name = "upload_time", nullable = false)
    private LocalDateTime uploadTime;

    /**
     * 上传人：Student表的主键id
     */
    @Column(name = "uploader_id", nullable = false)
    private Long uploaderId;

    /**
     * 关联的申请记录（Application主键）
     */
    @Column(name = "application_id")
    private Long applicationId;

    /**
     * 软删除标记
     */
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    /**
     * 关联的Application
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", insertable = false, updatable = false)
    private Application application;

    /**
     * 关联的Student
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader_id", insertable = false, updatable = false)
    private Student student;

    // 默认构造函数
    public FileRecord() {
    }

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getStoredFileName() {
        return storedFileName;
    }

    public void setStoredFileName(String storedFileName) {
        this.storedFileName = storedFileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Long getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(Long uploaderId) {
        this.uploaderId = uploaderId;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}