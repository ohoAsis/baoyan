package com.example.baoyan_assistant.service;

import com.example.baoyan_assistant.dto.CompetitionRecordDTO;
import com.example.baoyan_assistant.entity.CompetitionRecord;
import com.example.baoyan_assistant.entity.Student;
import com.example.baoyan_assistant.repository.CompetitionRecordRepository;
import com.example.baoyan_assistant.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 竞赛记录服务类
 */
@Service
public class CompetitionRecordService {

    @Autowired
    private CompetitionRecordRepository competitionRecordRepository;

    @Autowired
    private StudentRepository studentRepository;

    /**
     * 获取学生的所有竞赛记录
     * @param studentId 学生ID
     * @return 竞赛记录DTO列表
     */
    public List<CompetitionRecordDTO> getCompetitionsByStudentId(String studentId) {
        // 验证学生是否存在
        Optional<Student> student = studentRepository.findByStudentId(studentId);
        if (!student.isPresent()) {
            throw new RuntimeException("学生不存在，ID: " + studentId);
        }

        List<CompetitionRecord> competitions = competitionRecordRepository.findByStudentId(studentId);
        return competitions.stream()
                .map(CompetitionRecordDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 根据ID获取学生的特定竞赛记录
     * @param studentId 学生ID
     * @param competitionId 竞赛记录ID
     * @return 竞赛记录DTO
     */
    public CompetitionRecordDTO getCompetitionByIdAndStudentId(String studentId, Long competitionId) {
        // 验证学生是否存在
        Optional<Student> student = studentRepository.findByStudentId(studentId);
        if (!student.isPresent()) {
            throw new RuntimeException("学生不存在，ID: " + studentId);
        }

        CompetitionRecord competition = competitionRecordRepository.findByStudentIdAndId(studentId, competitionId);
        if (competition == null) {
            throw new RuntimeException("竞赛记录不存在，ID: " + competitionId);
        }

        return CompetitionRecordDTO.fromEntity(competition);
    }

    /**
     * 为学生添加竞赛记录
     * @param studentId 学生ID
     * @param competitionDTO 竞赛记录DTO
     * @return 创建的竞赛记录DTO
     */
    public CompetitionRecordDTO createCompetitionForStudent(String studentId, CompetitionRecordDTO competitionDTO) {
        // 验证学生是否存在
        Optional<Student> student = studentRepository.findByStudentId(studentId);
        if (!student.isPresent()) {
            throw new RuntimeException("学生不存在，ID: " + studentId);
        }

        CompetitionRecord competition = CompetitionRecordDTO.toEntity(competitionDTO);
        competition.setStudent(student.get());

        CompetitionRecord savedCompetition = competitionRecordRepository.save(competition);
        return CompetitionRecordDTO.fromEntity(savedCompetition);
    }

    /**
     * 更新竞赛记录
     * @param studentId 学生ID
     * @param competitionId 竞赛记录ID
     * @param competitionDTO 更新的竞赛记录DTO
     * @return 更新后的竞赛记录DTO
     */
    public CompetitionRecordDTO updateCompetition(String studentId, Long competitionId, CompetitionRecordDTO competitionDTO) {
        // 验证学生是否存在
        Optional<Student> student = studentRepository.findByStudentId(studentId);
        if (!student.isPresent()) {
            throw new RuntimeException("学生不存在，ID: " + studentId);
        }

        CompetitionRecord existingCompetition = competitionRecordRepository.findByStudentIdAndId(studentId, competitionId);
        if (existingCompetition == null) {
            throw new RuntimeException("竞赛记录不存在，ID: " + competitionId);
        }

        // 更新竞赛记录信息
        existingCompetition.setName(competitionDTO.getName());
        existingCompetition.setLevel(competitionDTO.getLevel());
        existingCompetition.setAward(competitionDTO.getAward());
        existingCompetition.setIsTeamProject(competitionDTO.getIsTeamProject());
        existingCompetition.setTeamRank(competitionDTO.getTeamRank());
        existingCompetition.setAwardDate(competitionDTO.getAwardDate());
        existingCompetition.setBaseScore(competitionDTO.getBaseScore());
        existingCompetition.setComputedScore(competitionDTO.getComputedScore());
        
        // 处理证据文件列表转换为字符串
        if (competitionDTO.getEvidenceFiles() != null && !competitionDTO.getEvidenceFiles().isEmpty()) {
            String files = String.join(",", competitionDTO.getEvidenceFiles());
            existingCompetition.setEvidenceFiles(files);
        }

        CompetitionRecord updatedCompetition = competitionRecordRepository.save(existingCompetition);
        return CompetitionRecordDTO.fromEntity(updatedCompetition);
    }

    /**
     * 删除竞赛记录
     * @param studentId 学生ID
     * @param competitionId 竞赛记录ID
     * @return 被删除的竞赛记录DTO
     */
    public CompetitionRecordDTO deleteCompetition(String studentId, Long competitionId) {
        // 验证学生是否存在
        Optional<Student> student = studentRepository.findByStudentId(studentId);
        if (!student.isPresent()) {
            throw new RuntimeException("学生不存在，ID: " + studentId);
        }

        CompetitionRecord competition = competitionRecordRepository.findByStudentIdAndId(studentId, competitionId);
        if (competition == null) {
            throw new RuntimeException("竞赛记录不存在，ID: " + competitionId);
        }

        CompetitionRecordDTO deletedCompetition = CompetitionRecordDTO.fromEntity(competition);
        competitionRecordRepository.delete(competition);
        return deletedCompetition;
    }
}