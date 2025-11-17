package com.example.baoyan_assistant.controller;

import com.example.baoyan_assistant.dto.CompetitionRecordDTO;
import com.example.baoyan_assistant.service.CompetitionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 竞赛记录控制器
 */
@RestController
@RequestMapping("/api/students/{id}/competitions")
public class CompetitionRecordController {

    @Autowired
    private CompetitionRecordService competitionRecordService;

    /**
     * 获取学生的所有竞赛记录
     * @param id 学生ID
     * @return 竞赛记录列表
     */
    @GetMapping
    public ResponseEntity<List<CompetitionRecordDTO>> getCompetitionsByStudentId(@PathVariable Long id) {
        try {
            List<CompetitionRecordDTO> competitions = competitionRecordService.getCompetitionsByStudentId(id);
            return ResponseEntity.ok(competitions);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 获取学生的特定竞赛记录
     * @param id 学生ID
     * @param competitionId 竞赛记录ID
     * @return 竞赛记录
     */
    @GetMapping("/{competitionId}")
    public ResponseEntity<CompetitionRecordDTO> getCompetitionByIdAndStudentId(
            @PathVariable Long id, @PathVariable Long competitionId) {
        try {
            CompetitionRecordDTO competition = competitionRecordService.getCompetitionByIdAndStudentId(id, competitionId);
            return ResponseEntity.ok(competition);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 为学生添加竞赛记录
     * @param id 学生ID
     * @param competitionDTO 竞赛记录DTO
     * @return 创建的竞赛记录
     */
    @PostMapping
    public ResponseEntity<CompetitionRecordDTO> createCompetitionForStudent(
            @PathVariable Long id, @RequestBody CompetitionRecordDTO competitionDTO) {
        try {
            CompetitionRecordDTO createdCompetition = competitionRecordService.createCompetitionForStudent(id, competitionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCompetition);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 更新竞赛记录
     * @param id 学生ID
     * @param competitionId 竞赛记录ID
     * @param competitionDTO 更新的竞赛记录DTO
     * @return 更新后的竞赛记录
     */
    @PutMapping("/{competitionId}")
    public ResponseEntity<CompetitionRecordDTO> updateCompetition(
            @PathVariable Long id, @PathVariable Long competitionId, @RequestBody CompetitionRecordDTO competitionDTO) {
        try {
            CompetitionRecordDTO updatedCompetition = competitionRecordService.updateCompetition(id, competitionId, competitionDTO);
            return ResponseEntity.ok(updatedCompetition);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 删除竞赛记录
     * @param id 学生ID
     * @param competitionId 竞赛记录ID
     * @return 被删除的竞赛记录
     */
    @DeleteMapping("/{competitionId}")
    public ResponseEntity<CompetitionRecordDTO> deleteCompetition(
            @PathVariable Long id, @PathVariable Long competitionId) {
        try {
            CompetitionRecordDTO deletedCompetition = competitionRecordService.deleteCompetition(id, competitionId);
            return ResponseEntity.ok(deletedCompetition);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}