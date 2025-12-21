package com.example.baoyan_assistant.interceptor;

import com.example.baoyan_assistant.util.JwtUtil;
import com.example.baoyan_assistant.util.UserContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;

/**
 * 用户身份认证拦截器
 * 统一拦截所有/api/**请求，验证用户身份信息
 */
@Component
public class UserAuthInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(UserAuthInterceptor.class);

    // 允许的角色列表
    private static final Set<String> ALLOWED_ROLES = Set.of("student", "reviewer", "admin");

    @Autowired
    private JwtUtil jwtUtil;

    // 不需要认证的路径
    private static final Set<String> EXCLUDED_PATHS = Set.of("/api/auth/login");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        logger.debug("拦截请求: {}", requestURI);

        // 检查是否需要跳过认证
        for (String excludedPath : EXCLUDED_PATHS) {
            if (requestURI.startsWith(excludedPath)) {
                logger.debug("跳过认证: {}", requestURI);
                return true;
            }
        }

        // 从Authorization头读取Bearer token
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            logger.warn("请求缺少有效的Authorization头: {}", requestURI);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"缺少有效的认证令牌\"}");
            return false;
        }

        String token = authorizationHeader.substring(7); // 去掉 "Bearer " 前缀

        try {
            // 解析JWT令牌
            Claims claims = jwtUtil.parseToken(token);

            // 取出token中的用户信息
            Long userId = claims.get("userId", Long.class);
            String userRole = claims.get("role", String.class);

            // 校验用户信息是否存在
            if (userId == null) {
                logger.warn("JWT令牌缺少userId: {}", requestURI);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"无效的认证令牌\"}");
                return false;
            }

            if (userRole == null || userRole.isEmpty()) {
                logger.warn("JWT令牌缺少role: {}", requestURI);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"无效的认证令牌\"}");
                return false;
            }

            // 校验角色是否合法
            if (!ALLOWED_ROLES.contains(userRole)) {
                logger.warn("JWT令牌包含非法角色: {}, 角色: {}", requestURI, userRole);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"非法角色\"}");
                return false;
            }

            // 将用户信息注入ThreadLocal
            UserContext.setUserId(userId);
            UserContext.setUserRole(userRole);
            logger.debug("用户身份认证通过: userId={}, userRole={}, requestURI={}", userId, userRole, requestURI);

            return true;
        } catch (JwtException e) {
            logger.warn("JWT令牌无效: {}, 错误: {}", requestURI, e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"无效的认证令牌\"}");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清理ThreadLocal，防止线程复用污染
        UserContext.clear();
        logger.debug("清理用户上下文: {}", request.getRequestURI());
    }
}