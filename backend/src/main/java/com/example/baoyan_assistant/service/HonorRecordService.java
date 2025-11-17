package com.example.baoyan_assistant.service;

import com.example.baoyan_assistant.dto.HonorRecordDTO;
import com.example.baoyan_assistant.entity.HonorRecord;
import com.example.baoyan_assistant.entity.Student;
import com.example.baoyan_assistant.repository.HonorRecordRepository;
import com.example.baoyan_assistant.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 荣誉记录服务类
 */
@Service
public class HonorRecordService {

    @Autowired
    private HonorRecordRepository honorRecordRepository;

    @Autowired
    private StudentRepository studentRepository;

    /**
     * 获取学生的所有荣誉记录
     * @param studentId 学生ID
     * @return 荣誉记录DTO列表
     */
    public List<HonorRecordDTO> getHonorsByStudentId(Long studentId) {
        // 验证学生是否存在
        Optional<Student> student = studentRepository.findById(studentId);
        if (!student.isPresent()) {
            throw new RuntimeException("学生不存在，ID: " + studentId);
        }

        List<HonorRecord> honors = honorRecordRepository.findByStudentId(studentId);
        return honors.stream()
                .map(HonorRecordDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 根据ID获取学生的特定荣誉记录
     * @param studentId 学生ID
     * @param honorId 荣誉记录ID
     * @return 荣誉记录DTO
     */
    public HonorRecordDTO getHonorByIdAndStudentId(Long studentId, Long honorId) {
        // 验证学生是否存在
        Optional<Student> student = studentRepository.findById(studentId);
        if (!student.isPresent()) {
            throw new RuntimeException("学生不存在，ID: " + studentId);
        }

        HonorRecord honor = honorRecordRepository.findByStudentIdAndId(studentId, honorId);
        if (honor == null) {
            throw new RuntimeException("荣誉记录不存在，ID: " + honorId);
        }

        return HonorRecordDTO.fromEntity(honor);
    }

    /**
     * 为学生添加荣誉记录
     * @param studentId 学生ID
     * @param honorDTO 荣誉记录DTO
     * @return 创建的荣誉记录DTO
     */
    public HonorRecordDTO createHonorForStudent(Long studentId, HonorRecordDTO honorDTO) {
        // 验证学生是否存在
        Optional<Student> student = studentRepository.findById(studentId);
        if (!student.isPresent()) {
            throw new RuntimeException("学生不存在，ID: " + studentId);
        }

        HonorRecord honor = HonorRecordDTO.toEntity(honorDTO);
        honor.setStudent(student.get());

        HonorRecord savedHonor = honorRecordRepository.save(honor);
        return HonorRecordDTO.fromEntity(savedHonor);
    }

    /**
     * 更新荣誉记录
     * @param studentId 学生ID
     * @param honorId 荣誉记录ID
     * @param honorDTO 更新的荣誉记录DTO
     * @return 更新后的荣誉记录DTO
     */
    public HonorRecordDTO updateHonor(Long studentId, Long honorId, HonorRecordDTO honorDTO) {
        // 验证学生是否存在
        Optional<Student> student = studentRepository.findById(studentId);
        if (!student.isPresent()) {
            throw new RuntimeException("学生不存在，ID: " + studentId);
        }

        HonorRecord existingHonor = honorRecordRepository.findByStudentIdAndId(studentId, honorId);
        if (existingHonor == null) {
            throw new RuntimeException("荣誉记录不存在，ID: " + honorId);
        }

        // 更新荣誉记录信息
        existingHonor.setTitle(honorDTO.getTitle());
        existingHonor.setLevel(honorDTO.getLevel());
        existingHonor.setDate(honorDTO.getDate());
        existingHonor.setBaseScore(honorDTO.getBaseScore());
        existingHonor.setComputedScore(honorDTO.getComputedScore());
        
        // 处理证据文件列表转换为字符串
        if (honorDTO.getEvidenceFiles() != null && !honorDTO.getEvidenceFiles().isEmpty()) {
            String files = String.join(",", honorDTO.getEvidenceFiles());
            existingHonor.setEvidenceFiles(files);
        }

        HonorRecord updatedHonor = honorRecordRepository.save(existingHonor);
        return HonorRecordDTO.fromEntity(updatedHonor);
    }

    /**
     * 删除荣誉记录
     * @param studentId 学生ID
     * @param honorId 荣誉记录ID
     * @return 被删除的荣誉记录DTO
     */
    public HonorRecordDTO deleteHonor(Long studentId, Long honorId) {
        // 验证学生是否存在
        Optional<Student> student = studentRepository.findById(studentId);
        if (!student.isPresent()) {
            throw new RuntimeException("学生不存在，ID: " + studentId);
        }

        HonorRecord honor = honorRecordRepository.findByStudentIdAndId(studentId, honorId);
        if (honor == null) {
            throw new RuntimeException("荣誉记录不存在，ID: " + honorId);
        }

        HonorRecordDTO deletedHonor = HonorRecordDTO.fromEntity(honor);
        honorRecordRepository.delete(honor);
        return deletedHonor;
    }
}