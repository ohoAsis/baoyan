package com.example.baoyan_assistant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

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
                    // 允许登录接口无需认证
                    .requestMatchers("/api/auth/login").permitAll()
                    // 其他/api/**接口需要认证
                    .requestMatchers("/api/**").authenticated()
                    // 其他请求都允许
                    .anyRequest().permitAll()
            )
            // 配置未认证时的处理方式：返回401而不是302重定向
            .exceptionHandling(exceptionHandling ->
                exceptionHandling.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            );

        return http.build();
    }
}
