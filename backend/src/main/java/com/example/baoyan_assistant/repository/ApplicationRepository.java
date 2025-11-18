package com.example.baoyan_assistant.repository;

import com.example.baoyan_assistant.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 申请数据访问接口
 * 提供申请信息的数据库操作方法
 */
@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    
    /**
     * 根据学生ID查询申请列表
     * @param studentId 学生ID
     * @return 申请列表
     */
    List<Application> findByStudentId(Long studentId);
    
    /**
     * 根据学生ID和状态查询申请列表
     * @param studentId 学生ID
     * @param status 申请状态
     * @return 申请列表
     */
    List<Application> findByStudentIdAndStatus(Long studentId, String status);
    
    /**
     * 根据状态查询申请列表
     * @param status 申请状态
     * @return 申请列表
     */
    List<Application> findByStatus(String status);
    
    /**
     * 根据申请类型查询申请列表
     * @param type 申请类型
     * @return 申请列表
     */
    List<Application> findByType(String type);
    
    /**
     * 根据学生ID和申请类型查询申请列表
     * @param studentId 学生ID
     * @param type 申请类型
     * @return 申请列表
     */
    List<Application> findByStudentIdAndType(Long studentId, String type);
}