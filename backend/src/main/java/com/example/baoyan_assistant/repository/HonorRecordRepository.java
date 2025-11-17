package com.example.baoyan_assistant.repository;

import com.example.baoyan_assistant.entity.HonorRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 荣誉记录数据访问接口
 */
@Repository
public interface HonorRecordRepository extends JpaRepository<HonorRecord, Long> {
    
    /**
     * 根据学生ID查询荣誉记录
     * @param studentId 学生ID
     * @return 荣誉记录列表
     */
    List<HonorRecord> findByStudentId(Long studentId);
    
    /**
     * 根据学生ID和荣誉记录ID查询荣誉记录
     * @param studentId 学生ID
     * @param id 荣誉记录ID
     * @return 荣誉记录
     */
    HonorRecord findByStudentIdAndId(Long studentId, Long id);
}