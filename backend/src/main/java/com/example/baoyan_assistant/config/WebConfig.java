package com.example.baoyan_assistant.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * Web配置类
 * 配置静态资源处理器，用于提供上传的文件访问
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload.dir:uploads}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置上传文件的静态资源访问路径
        // 将 /uploads/** 映射到文件系统的 uploads 目录
        String uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize().toString();
        
        // Windows路径需要转换为正斜杠，并确保以 / 结尾
        uploadPath = uploadPath.replace("\\", "/");
        if (!uploadPath.endsWith("/")) {
            uploadPath += "/";
        }
        
        // 使用 file: 协议访问本地文件系统
        // Windows路径需要三个斜杠：file:///C:/path/to/uploads/
        String fileUrl = uploadPath.startsWith("/") ? "file:" + uploadPath : "file:/" + uploadPath;
        
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(fileUrl)
                .setCachePeriod(3600); // 缓存1小时
        
        // 添加日志输出以便调试
        System.out.println("静态资源配置: /uploads/** -> " + fileUrl);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 允许跨域访问（如果需要）
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}

