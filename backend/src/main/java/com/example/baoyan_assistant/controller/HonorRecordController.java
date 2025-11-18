package com.example.baoyan_assistant.controller;

import com.example.baoyan_assistant.dto.HonorRecordDTO;
import com.example.baoyan_assistant.service.HonorRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 荣誉记录控制器
 */
@RestController
@RequestMapping("/api/students/{id}/honors")
public class HonorRecordController {

    @Autowired
    private HonorRecordService honorRecordService;

    /**
     * 获取学生的所有荣誉记录
     * @param id 学生ID
     * @return 荣誉记录列表
     */
    @GetMapping
    public ResponseEntity<List<HonorRecordDTO>> getHonorsByStudentId(@PathVariable String id) {
        try {
            List<HonorRecordDTO> honors = honorRecordService.getHonorsByStudentId(id);
            return ResponseEntity.ok(honors);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 获取学生的特定荣誉记录
     * @param id 学生ID
     * @param honorId 荣誉记录ID
     * @return 荣誉记录
     */
    @GetMapping("/{honorId}")
    public ResponseEntity<HonorRecordDTO> getHonorByIdAndStudentId(
            @PathVariable String id, @PathVariable Long honorId) {
        try {
            HonorRecordDTO honor = honorRecordService.getHonorByIdAndStudentId(id, honorId);
            return ResponseEntity.ok(honor);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 为学生添加荣誉记录
     * @param id 学生ID
     * @param honorDTO 荣誉记录DTO
     * @return 创建的荣誉记录
     */
    @PostMapping
    public ResponseEntity<HonorRecordDTO> createHonorForStudent(
            @PathVariable String id, @RequestBody HonorRecordDTO honorDTO) {
        try {
            HonorRecordDTO createdHonor = honorRecordService.createHonorForStudent(id, honorDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdHonor);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 更新荣誉记录
     * @param id 学生ID
     * @param honorId 荣誉记录ID
     * @param honorDTO 更新的荣誉记录DTO
     * @return 更新后的荣誉记录
     */
    @PutMapping("/{honorId}")
    public ResponseEntity<HonorRecordDTO> updateHonor(
            @PathVariable String id, @PathVariable Long honorId, @RequestBody HonorRecordDTO honorDTO) {
        try {
            HonorRecordDTO updatedHonor = honorRecordService.updateHonor(id, honorId, honorDTO);
            return ResponseEntity.ok(updatedHonor);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 删除荣誉记录
     * @param id 学生ID
     * @param honorId 荣誉记录ID
     * @return 被删除的荣誉记录
     */
    @DeleteMapping("/{honorId}")
    public ResponseEntity<HonorRecordDTO> deleteHonor(
            @PathVariable String id, @PathVariable Long honorId) {
        try {
            HonorRecordDTO deletedHonor = honorRecordService.deleteHonor(id, honorId);
            return ResponseEntity.ok(deletedHonor);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}