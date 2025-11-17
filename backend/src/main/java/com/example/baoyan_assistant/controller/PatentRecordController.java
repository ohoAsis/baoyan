package com.example.baoyan_assistant.controller;

import com.example.baoyan_assistant.dto.PatentRecordDTO;
import com.example.baoyan_assistant.service.PatentRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 专利记录控制器
 */
@RestController
@RequestMapping("/api/students/{id}/patents")
public class PatentRecordController {

    @Autowired
    private PatentRecordService patentRecordService;

    /**
     * 获取学生的所有专利记录
     * @param id 学生ID
     * @return 专利记录列表
     */
    @GetMapping
    public ResponseEntity<List<PatentRecordDTO>> getPatentsByStudentId(@PathVariable Long id) {
        try {
            List<PatentRecordDTO> patents = patentRecordService.getPatentsByStudentId(id);
            return ResponseEntity.ok(patents);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 获取学生的特定专利记录
     * @param id 学生ID
     * @param patentId 专利记录ID
     * @return 专利记录
     */
    @GetMapping("/{patentId}")
    public ResponseEntity<PatentRecordDTO> getPatentByIdAndStudentId(
            @PathVariable Long id, @PathVariable Long patentId) {
        try {
            PatentRecordDTO patent = patentRecordService.getPatentByIdAndStudentId(id, patentId);
            return ResponseEntity.ok(patent);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 为学生添加专利记录
     * @param id 学生ID
     * @param patentDTO 专利记录DTO
     * @return 创建的专利记录
     */
    @PostMapping
    public ResponseEntity<PatentRecordDTO> createPatentForStudent(
            @PathVariable Long id, @RequestBody PatentRecordDTO patentDTO) {
        try {
            PatentRecordDTO createdPatent = patentRecordService.createPatentForStudent(id, patentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPatent);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 更新专利记录
     * @param id 学生ID
     * @param patentId 专利记录ID
     * @param patentDTO 更新的专利记录DTO
     * @return 更新后的专利记录
     */
    @PutMapping("/{patentId}")
    public ResponseEntity<PatentRecordDTO> updatePatent(
            @PathVariable Long id, @PathVariable Long patentId, @RequestBody PatentRecordDTO patentDTO) {
        try {
            PatentRecordDTO updatedPatent = patentRecordService.updatePatent(id, patentId, patentDTO);
            return ResponseEntity.ok(updatedPatent);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 删除专利记录
     * @param id 学生ID
     * @param patentId 专利记录ID
     * @return 被删除的专利记录
     */
    @DeleteMapping("/{patentId}")
    public ResponseEntity<PatentRecordDTO> deletePatent(
            @PathVariable Long id, @PathVariable Long patentId) {
        try {
            PatentRecordDTO deletedPatent = patentRecordService.deletePatent(id, patentId);
            return ResponseEntity.ok(deletedPatent);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}