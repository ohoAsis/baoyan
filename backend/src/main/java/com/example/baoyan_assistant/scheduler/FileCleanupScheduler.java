package com.example.baoyan_assistant.scheduler;

import com.example.baoyan_assistant.service.FileCleanupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 文件清理调度器
 * 负责定期触发文件清理任务
 */
@Component
public class FileCleanupScheduler {

    private static final Logger logger = LoggerFactory.getLogger(FileCleanupScheduler.class);

    @Autowired
    private FileCleanupService fileCleanupService;

    /**
     * 定时执行文件清理任务
     * 每天凌晨3点执行
     * cron表达式：0 0 3 * * ?
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void scheduleFileCleanup() {
        logger.info("=== 开始执行定时文件清理任务 ===");
        
        try {
            fileCleanupService.cleanAll();
            logger.info("=== 定时文件清理任务执行完成 ===");
        } catch (Exception e) {
            logger.error("=== 定时文件清理任务执行失败: {} ===", e.getMessage(), e);
        }
    }
}