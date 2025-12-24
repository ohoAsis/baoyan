package com.example.baoyan_assistant.config;

import com.example.baoyan_assistant.entity.User;
import com.example.baoyan_assistant.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import org.mindrot.jbcrypt.BCrypt;

/**
 * 系统管理员初始化组件
 * 在项目启动后自动检查并创建默认管理员账号
 */
@Component
public class AdminInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(AdminInitializer.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 检查是否存在管理员账号
        if (!userRepository.existsByRole("admin")) {
            // 创建默认管理员账号
            User admin = new User();
            admin.setUsername("admin");
            admin.setRealName("系统管理员");
            admin.setRole("admin");
            admin.setStatus("active");
            admin.setPassword(BCrypt.hashpw("123456", BCrypt.gensalt()));
            LocalDateTime now = LocalDateTime.now();
            admin.setCreateTime(now);
            admin.setUpdateTime(now);

            // 保存到数据库
            userRepository.save(admin);

            // 输出创建成功信息
            logger.info("Initialized default admin account: username=admin, password=123456");
        }
    }
}