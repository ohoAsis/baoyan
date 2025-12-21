package com.example.baoyan_assistant.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * 用于生成、解析和验证JWT令牌
 */
@Component
public class JwtUtil {

    // 密钥（生产环境应从配置文件读取）
    private static final String SECRET_KEY = "baoyan_assistant_secret_key_1234567890";
    // 过期时间（7天）
    private static final long EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;
    // 生成密钥对象
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    /**
     * 生成JWT令牌
     * @param userId 用户ID
     * @param role 用户角色
     * @return JWT令牌
     */
    public String generateToken(Long userId, String role) {
        // 生成令牌
        return Jwts.builder()
                .claim("userId", userId)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .signWith(KEY)
                .compact();
    }

    /**
     * 解析JWT令牌
     * @param token JWT令牌
     * @return JWT声明
     * @throws JwtException 如果令牌无效
     */
    public Claims parseToken(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 检查JWT令牌是否过期
     * @param token JWT令牌
     * @return 如果过期返回true，否则返回false
     */
    public boolean isExpired(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().before(new Date());
        } catch (JwtException e) {
            return true;
        }
    }
}
