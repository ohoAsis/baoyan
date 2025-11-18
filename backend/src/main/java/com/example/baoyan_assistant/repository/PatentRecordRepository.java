package com.example.baoyan_assistant.repository;

import com.example.baoyan_assistant.entity.PatentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 专利记录数据访问接口
 */
@Repository
public interface PatentRecordRepository extends JpaRepository<PatentRecord, Long> {
    
    /**
     * 根据学生ID查询专利记录
     * @param studentId 学生ID
     * @return 专利记录列表
     */
    @Query("SELECT p FROM PatentRecord p WHERE p.student.studentId = :studentId")
    List<PatentRecord> findByStudentId(@Param("studentId") String studentId);
    
    /**
     * 根据学生ID和专利记录ID查询专利记录
     * @param studentId 学生ID
     * @param id 专利记录ID
     * @return 专利记录
     */
    @Query("SELECT p FROM PatentRecord p WHERE p.student.studentId = :studentId AND p.id = :id")
    PatentRecord findByStudentIdAndId(@Param("studentId") String studentId, @Param("id") Long id);
}