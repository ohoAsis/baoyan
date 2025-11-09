package com.example.baoyan_assistant.repository;

import com.example.baoyan_assistant.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * 学生数据访问接口
 * 提供学生信息的数据库操作方法
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    /**
     * 根据学号查询学生（学号唯一）
     * @param studentId 学号
     * @return 学生对象（如果存在）
     */
    Optional<Student> findByStudentId(String studentId);
    
    /**
     * 根据姓名模糊查询学生列表
     * @param name 姓名（支持模糊匹配）
     * @return 学生列表
     */
    List<Student> findByNameContaining(String name);
    
    /**
     * 根据专业查询学生列表
     * @param major 专业名称
     * @return 学生列表
     */
    List<Student> findByMajor(String major);
    
    /**
     * 根据年级查询学生列表
     * @param grade 年级（如"2021级"）
     * @return 学生列表
     */
    List<Student> findByGrade(String grade);
    
    /**
     * 根据申请状态查询学生列表
     * @param applicationStatus 申请状态（草稿、已提交、审核中、通过、驳回）
     * @return 学生列表
     */
    List<Student> findByApplicationStatus(String applicationStatus);
    
    /**
     * 检查学号是否存在
     * @param studentId 学号
     * @return 如果存在返回 true，否则返回 false
     */
    boolean existsByStudentId(String studentId);
}

