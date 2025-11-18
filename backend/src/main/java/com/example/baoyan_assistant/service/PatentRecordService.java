package com.example.baoyan_assistant.service;

import com.example.baoyan_assistant.dto.PatentRecordDTO;
import com.example.baoyan_assistant.entity.PatentRecord;
import com.example.baoyan_assistant.entity.Student;
import com.example.baoyan_assistant.repository.PatentRecordRepository;
import com.example.baoyan_assistant.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 专利记录服务类
 */
@Service
public class PatentRecordService {

    @Autowired
    private PatentRecordRepository patentRecordRepository;

    @Autowired
    private StudentRepository studentRepository;

    /**
     * 获取学生的所有专利记录
     * @param studentId 学生ID
     * @return 专利记录DTO列表
     */
    public List<PatentRecordDTO> getPatentsByStudentId(String studentId) {
        // 验证学生是否存在
        Optional<Student> student = studentRepository.findByStudentId(studentId);
        if (!student.isPresent()) {
            throw new RuntimeException("学生不存在，ID: " + studentId);
        }

        List<PatentRecord> patents = patentRecordRepository.findByStudentId(studentId);
        return patents.stream()
                .map(PatentRecordDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 根据ID获取学生的特定专利记录
     * @param studentId 学生ID
     * @param patentId 专利记录ID
     * @return 专利记录DTO
     */
    public PatentRecordDTO getPatentByIdAndStudentId(String studentId, Long patentId) {
        // 验证学生是否存在
        Optional<Student> student = studentRepository.findByStudentId(studentId);
        if (!student.isPresent()) {
            throw new RuntimeException("学生不存在，ID: " + studentId);
        }

        PatentRecord patent = patentRecordRepository.findByStudentIdAndId(studentId, patentId);
        if (patent == null) {
            throw new RuntimeException("专利记录不存在，ID: " + patentId);
        }

        return PatentRecordDTO.fromEntity(patent);
    }

    /**
     * 为学生添加专利记录
     * @param studentId 学生ID
     * @param patentDTO 专利记录DTO
     * @return 创建的专利记录DTO
     */
    public PatentRecordDTO createPatentForStudent(String studentId, PatentRecordDTO patentDTO) {
        // 验证学生是否存在
        Optional<Student> student = studentRepository.findByStudentId(studentId);
        if (!student.isPresent()) {
            throw new RuntimeException("学生不存在，ID: " + studentId);
        }

        PatentRecord patent = PatentRecordDTO.toEntity(patentDTO);
        patent.setStudent(student.get());

        PatentRecord savedPatent = patentRecordRepository.save(patent);
        return PatentRecordDTO.fromEntity(savedPatent);
    }

    /**
     * 更新专利记录
     * @param studentId 学生ID
     * @param patentId 专利记录ID
     * @param patentDTO 更新的专利记录DTO
     * @return 更新后的专利记录DTO
     */
    public PatentRecordDTO updatePatent(String studentId, Long patentId, PatentRecordDTO patentDTO) {
        // 验证学生是否存在
        Optional<Student> student = studentRepository.findByStudentId(studentId);
        if (!student.isPresent()) {
            throw new RuntimeException("学生不存在，ID: " + studentId);
        }

        PatentRecord existingPatent = patentRecordRepository.findByStudentIdAndId(studentId, patentId);
        if (existingPatent == null) {
            throw new RuntimeException("专利记录不存在，ID: " + patentId);
        }

        // 更新专利记录信息
        existingPatent.setTitle(patentDTO.getTitle());
        existingPatent.setType(patentDTO.getType());
        existingPatent.setAuthorizationDate(patentDTO.getAuthorizationDate());
        existingPatent.setIsFirstAuthor(patentDTO.getIsFirstAuthor());
        existingPatent.setBaseScore(patentDTO.getBaseScore());
        existingPatent.setComputedScore(patentDTO.getComputedScore());
        
        // 处理证据文件列表转换为字符串
        if (patentDTO.getEvidenceFiles() != null && !patentDTO.getEvidenceFiles().isEmpty()) {
            String files = String.join(",", patentDTO.getEvidenceFiles());
            existingPatent.setEvidenceFiles(files);
        }

        PatentRecord updatedPatent = patentRecordRepository.save(existingPatent);
        return PatentRecordDTO.fromEntity(updatedPatent);
    }

    /**
     * 删除专利记录
     * @param studentId 学生ID
     * @param patentId 专利记录ID
     * @return 被删除的专利记录DTO
     */
    public PatentRecordDTO deletePatent(String studentId, Long patentId) {
        // 验证学生是否存在
        Optional<Student> student = studentRepository.findByStudentId(studentId);
        if (!student.isPresent()) {
            throw new RuntimeException("学生不存在，ID: " + studentId);
        }

        PatentRecord patent = patentRecordRepository.findByStudentIdAndId(studentId, patentId);
        if (patent == null) {
            throw new RuntimeException("专利记录不存在，ID: " + patentId);
        }

        PatentRecordDTO deletedPatent = PatentRecordDTO.fromEntity(patent);
        patentRecordRepository.delete(patent);
        return deletedPatent;
    }
}