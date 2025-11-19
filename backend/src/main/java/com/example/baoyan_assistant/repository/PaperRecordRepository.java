package com.example.baoyan_assistant.repository;

import com.example.baoyan_assistant.entity.PaperRecord;
import com.example.baoyan_assistant.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 论文记录数据访问接口
 */
@Repository
public interface PaperRecordRepository extends JpaRepository<PaperRecord, Long> {
    
    /**
     * 根据学生ID查询论文记录
     * @param studentId 学生ID
     * @return 论文记录列表
     */
    @Query("SELECT p FROM PaperRecord p WHERE p.student.studentId = :studentId")
    List<PaperRecord> findByStudentId(@Param("studentId") String studentId);
    
    /**
     * 根据学生ID和论文ID查询论文记录
     * @param studentId 学生ID
     * @param id 论文ID
     * @return 论文记录
     */
    @Query("SELECT p FROM PaperRecord p WHERE p.student.studentId = :studentId AND p.id = :id")
    PaperRecord findByStudentIdAndId(@Param("studentId") String studentId, @Param("id") Long id);
}