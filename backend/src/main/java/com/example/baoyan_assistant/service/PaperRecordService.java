package com.example.baoyan_assistant.service;

import com.example.baoyan_assistant.entity.PaperRecord;
import com.example.baoyan_assistant.entity.Student;
import com.example.baoyan_assistant.repository.PaperRecordRepository;
import com.example.baoyan_assistant.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 论文记录服务类
 */
@Service
public class PaperRecordService {
    
    @Autowired
    private PaperRecordRepository paperRecordRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    /**
     * 根据学生ID获取所有论文记录
     * @param studentId 学生ID
     * @return 论文记录列表
     */
    public List<PaperRecord> getPapersByStudentId(Long studentId) {
        return paperRecordRepository.findByStudentId(studentId);
    }
    
    /**
     * 根据学生ID和论文ID获取论文记录
     * @param studentId 学生ID
     * @param paperId 论文ID
     * @return 论文记录
     */
    public Optional<PaperRecord> getPaperByIdAndStudentId(Long studentId, Long paperId) {
        return Optional.ofNullable(paperRecordRepository.findByStudentIdAndId(studentId, paperId));
    }
    
    /**
     * 为学生添加论文记录
     * @param studentId 学生ID
     * @param paperRecord 论文记录
     * @return 保存后的论文记录
     */
    public PaperRecord createPaperForStudent(Long studentId, PaperRecord paperRecord) {
        // 验证学生是否存在
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isEmpty()) {
            throw new RuntimeException("学生不存在，ID: " + studentId);
        }
        
        // 设置学生关联
        paperRecord.setStudent(studentOptional.get());
        
        // 保存论文记录
        return paperRecordRepository.save(paperRecord);
    }
    
    /**
     * 更新论文记录
     * @param studentId 学生ID
     * @param paperId 论文ID
     * @param paperRecord 更新的论文记录
     * @return 更新后的论文记录
     */
    public PaperRecord updatePaper(Long studentId, Long paperId, PaperRecord paperRecord) {
        // 验证论文记录是否存在且属于该学生
        Optional<PaperRecord> existingPaperOptional = getPaperByIdAndStudentId(studentId, paperId);
        if (existingPaperOptional.isEmpty()) {
            throw new RuntimeException("论文记录不存在或不属于该学生");
        }
        
        PaperRecord existingPaper = existingPaperOptional.get();
        
        // 更新字段
        existingPaper.setTitle(paperRecord.getTitle());
        existingPaper.setLevel(paperRecord.getLevel());
        existingPaper.setPublication(paperRecord.getPublication());
        existingPaper.setPublishDate(paperRecord.getPublishDate());
        existingPaper.setAuthorOrder(paperRecord.getAuthorOrder());
        existingPaper.setTotalAuthors(paperRecord.getTotalAuthors());
        existingPaper.setUnitIsFirstUnit(paperRecord.getUnitIsFirstUnit());
        existingPaper.setBaseScore(paperRecord.getBaseScore());
        existingPaper.setComputedScore(paperRecord.getComputedScore());
        existingPaper.setEvidenceFiles(paperRecord.getEvidenceFiles());
        
        // 保存更新
        return paperRecordRepository.save(existingPaper);
    }
    
    /**
     * 删除论文记录
     * @param studentId 学生ID
     * @param paperId 论文ID
     * @return 被删除的论文记录
     */
    public PaperRecord deletePaper(Long studentId, Long paperId) {
        // 验证论文记录是否存在且属于该学生
        Optional<PaperRecord> existingPaperOptional = getPaperByIdAndStudentId(studentId, paperId);
        if (existingPaperOptional.isEmpty()) {
            throw new RuntimeException("论文记录不存在或不属于该学生");
        }
        
        PaperRecord paperToDelete = existingPaperOptional.get();
        paperRecordRepository.delete(paperToDelete);
        return paperToDelete;
    }
}