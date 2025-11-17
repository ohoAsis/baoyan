package com.example.baoyan_assistant.controller;

import com.example.baoyan_assistant.dto.CompetitionRecordDTO;
import com.example.baoyan_assistant.dto.DTOConverter;
import com.example.baoyan_assistant.entity.CompetitionRecord;
import com.example.baoyan_assistant.entity.Student;
import com.example.baoyan_assistant.service.CompetitionRecordService;
import com.example.baoyan_assistant.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students/{studentId}/competitions")
public class CompetitionRecordController {

    @Autowired
    private CompetitionRecordService competitionRecordService;
    
    @Autowired
    private StudentService studentService;

    /**
     * 获取学生的所有竞赛记录
     */
    @GetMapping
    public ResponseEntity<List<CompetitionRecordDTO>> getCompetitionsByStudentId(@PathVariable String studentId) {
        List<CompetitionRecord> competitions = competitionRecordService.getCompetitionsByStudentId(studentId);
        List<CompetitionRecordDTO> competitionDTOs = competitions.stream()
                .map(DTOConverter::convertToCompetitionRecordDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(competitionDTOs);
    }

    /**
     * 获取学生的特定竞赛记录
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompetitionRecordDTO> getCompetitionById(@PathVariable String studentId, @PathVariable Long id) {
        CompetitionRecord competition = competitionRecordService.getCompetitionById(id);
        if (competition != null && competition.getStudent().getStudentId().equals(studentId)) {
            return ResponseEntity.ok(DTOConverter.convertToCompetitionRecordDTO(competition));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 添加学生的竞赛记录
     */
    @PostMapping
    public ResponseEntity<CompetitionRecordDTO> addCompetition(@PathVariable String studentId, @RequestBody CompetitionRecordDTO competitionDTO) {
        Student student = studentService.getStudentByStudentId(studentId);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        
        CompetitionRecord competition = DTOConverter.convertToCompetitionRecordEntity(competitionDTO, student);
        CompetitionRecord savedCompetition = competitionRecordService.saveCompetition(competition);
        return ResponseEntity.ok(DTOConverter.convertToCompetitionRecordDTO(savedCompetition));
    }

    /**
     * 更新学生的竞赛记录
     */
    @PutMapping("/{id}")
    public ResponseEntity<CompetitionRecordDTO> updateCompetition(@PathVariable String studentId, @PathVariable Long id, @RequestBody CompetitionRecordDTO competitionDTO) {
        CompetitionRecord existingCompetition = competitionRecordService.getCompetitionById(id);
        if (existingCompetition == null || !existingCompetition.getStudent().getStudentId().equals(studentId)) {
            return ResponseEntity.notFound().build();
        }
        
        Student student = studentService.getStudentByStudentId(studentId);
        CompetitionRecord updatedCompetition = DTOConverter.convertToCompetitionRecordEntity(competitionDTO, student);
        updatedCompetition.setId(id);
        CompetitionRecord savedCompetition = competitionRecordService.saveCompetition(updatedCompetition);
        return ResponseEntity.ok(DTOConverter.convertToCompetitionRecordDTO(savedCompetition));
    }

    /**
     * 删除学生的竞赛记录
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompetition(@PathVariable String studentId, @PathVariable Long id) {
        CompetitionRecord competition = competitionRecordService.getCompetitionById(id);
        if (competition != null && competition.getStudent().getStudentId().equals(studentId)) {
            competitionRecordService.deleteCompetition(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}