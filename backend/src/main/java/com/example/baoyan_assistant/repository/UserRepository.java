package com.example.baoyan_assistant.repository;

import com.example.baoyan_assistant.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * 用户数据访问接口
 * 提供用户信息的数据库操作方法
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象（如果存在）
     */
    Optional<User> findByUsername(String username);

    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 如果存在返回 true，否则返回 false
     */
    boolean existsByUsername(String username);

    /**
     * 检查指定角色的用户是否存在
     * @param role 角色名称
     * @return 如果存在返回 true，否则返回 false
     */
    boolean existsByRole(String role);

    /**
     * 根据角色和关键字查询用户列表
     * @param role 角色（可选）
     * @param keyword 关键字（可选，模糊匹配用户名、真实姓名、学号或工号）
     * @return 用户列表
     */
    @Query("SELECT u FROM User u WHERE (:role IS NULL OR u.role = :role) AND (:keyword IS NULL OR u.username LIKE %:keyword% OR u.realName LIKE %:keyword% OR u.studentId LIKE %:keyword% OR u.teacherId LIKE %:keyword%)")
    List<User> findByRoleAndKeyword(@Param("role") String role, @Param("keyword") String keyword);
}
