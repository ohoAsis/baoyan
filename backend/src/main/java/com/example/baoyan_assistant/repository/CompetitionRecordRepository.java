package com.example.baoyan_assistant.repository;

import com.example.baoyan_assistant.entity.CompetitionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 竞赛记录数据访问接口
 */
@Repository
public interface CompetitionRecordRepository extends JpaRepository<CompetitionRecord, Long> {
    
    /**
     * 根据学生ID查询竞赛记录
     * @param studentId 学生ID
     * @return 竞赛记录列表
     */
    List<CompetitionRecord> findByStudentId(Long studentId);
    
    /**
     * 根据学生ID和竞赛记录ID查询竞赛记录
     * @param studentId 学生ID
     * @param id 竞赛记录ID
     * @return 竞赛记录
     */
    CompetitionRecord findByStudentIdAndId(Long studentId, Long id);
}