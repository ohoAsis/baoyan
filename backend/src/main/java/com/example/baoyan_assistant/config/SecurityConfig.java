package com.example.baoyan_assistant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 禁用 CSRF
            .csrf(csrf -> csrf.disable())
            // 禁用表单登录
            .formLogin(formLogin -> formLogin.disable())
            // 禁用 HTTP Basic
            .httpBasic(httpBasic -> httpBasic.disable())
            // 设置会话管理为无状态
            .sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            // 配置授权规则
            .authorizeHttpRequests(authorizeHttpRequests ->
                authorizeHttpRequests
                    // 所有请求都允许，因为/api/**已经由UserAuthInterceptor处理
                    .anyRequest().permitAll()
            );

        return http.build();
    }
}
