package com.example.baoyan_assistant.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

/**
 * 数据库文件权限修复器
 * 在应用启动时自动修复SQLite数据库文件的只读属性
 */
@Component
public class DatabaseFilePermissionFixer implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        fixDatabaseFilePermissions();
    }

    /**
     * 修复数据库文件权限
     */
    private void fixDatabaseFilePermissions() {
        try {
            // 从JDBC URL中提取数据库文件路径
            String dbPath = extractDbPath(datasourceUrl);
            
            if (dbPath == null) {
                System.out.println("无法从数据库URL中提取路径: " + datasourceUrl);
                return;
            }

            Path dbFilePath = Paths.get(dbPath);
            
            // 检查文件是否存在
            if (!Files.exists(dbFilePath)) {
                System.out.println("数据库文件不存在: " + dbFilePath);
                return;
            }

            // 修复文件权限
            File dbFile = dbFilePath.toFile();
            
            // Windows系统：移除只读属性
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                if (dbFile.setWritable(true)) {
                    System.out.println("✓ 已修复数据库文件权限: " + dbFilePath);
                } else {
                    System.err.println("✗ 无法修复数据库文件权限: " + dbFilePath);
                }
            } else {
                // Linux/Mac系统：设置文件权限为可读写
                try {
                    Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rw-rw-rw-");
                    Files.setPosixFilePermissions(dbFilePath, permissions);
                    System.out.println("✓ 已修复数据库文件权限: " + dbFilePath);
                } catch (Exception e) {
                    System.err.println("✗ 无法修复数据库文件权限: " + e.getMessage());
                }
            }

            // 同时修复数据库目录的权限
            Path dbDir = dbFilePath.getParent();
            if (dbDir != null && Files.exists(dbDir)) {
                File dir = dbDir.toFile();
                if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                    dir.setWritable(true);
                }
            }

        } catch (Exception e) {
            System.err.println("修复数据库文件权限时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从JDBC URL中提取数据库文件路径
     * 支持格式：
     * - jdbc:sqlite:./student.db
     * - jdbc:sqlite:student.db
     * - jdbc:sqlite:/path/to/student.db
     * - jdbc:sqlite:C:/path/to/student.db
     */
    private String extractDbPath(String jdbcUrl) {
        if (jdbcUrl == null || !jdbcUrl.startsWith("jdbc:sqlite:")) {
            return null;
        }

        String path = jdbcUrl.substring("jdbc:sqlite:".length());
        
        // 如果是相对路径（以 ./ 开头或直接是文件名）
        if (path.startsWith("./")) {
            path = path.substring(2);
        }
        
        // 转换为绝对路径
        Path currentDir = Paths.get("").toAbsolutePath();
        Path dbPath = currentDir.resolve(path).normalize();
        
        return dbPath.toString();
    }
}

