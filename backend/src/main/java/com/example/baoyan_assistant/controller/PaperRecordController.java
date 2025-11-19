package com.example.baoyan_assistant.controller;

import com.example.baoyan_assistant.dto.PaperRecordDTO;
import com.example.baoyan_assistant.entity.PaperRecord;
import com.example.baoyan_assistant.service.PaperRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 论文记录控制器
 * 处理论文相关的API请求
 */
@RestController
@RequestMapping("/api/students/{studentId}/papers")
public class PaperRecordController {
    
    @Autowired
    private PaperRecordService paperRecordService;
    
    /**
     * 获取学生的所有论文记录
     * GET /api/students/{studentId}/papers
     * @param studentId 学生ID
     * @return 论文记录列表
     */
    @GetMapping
    public ResponseEntity<List<PaperRecordDTO>> getAllPapersByStudentId(@PathVariable String studentId) {
        try {
            List<PaperRecord> papers = paperRecordService.getPapersByStudentId(studentId);
            List<PaperRecordDTO> paperDTOs = papers.stream()
                    .map(PaperRecordDTO::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(paperDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 获取学生的特定论文记录
     * GET /api/students/{studentId}/papers/{paperId}
     * @param studentId 学生ID
     * @param paperId 论文ID
     * @return 论文记录
     */
    @GetMapping("/{paperId}")
    public ResponseEntity<PaperRecordDTO> getPaperById(@PathVariable String studentId, @PathVariable Long paperId) {
        try {
            return paperRecordService.getPaperByIdAndStudentId(studentId, paperId)
                    .map(paper -> ResponseEntity.ok(PaperRecordDTO.fromEntity(paper)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 为学生添加论文记录
     * POST /api/students/{studentId}/papers
     * @param studentId 学生ID
     * @param paperRecordDTO 论文记录DTO
     * @return 创建的论文记录
     */
    @PostMapping
    public ResponseEntity<?> createPaper(@PathVariable String studentId, @RequestBody PaperRecordDTO paperRecordDTO) {
        try {
            // 将DTO转换为实体
            PaperRecord paperRecord = paperRecordDTO.toEntity();
            
            // 创建论文记录
            PaperRecord savedPaper = paperRecordService.createPaperForStudent(studentId, paperRecord);
            
            // 将实体转换回DTO返回
            PaperRecordDTO responseDTO = PaperRecordDTO.fromEntity(savedPaper);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "创建论文记录失败");
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 更新论文记录
     * PUT /api/students/{studentId}/papers/{paperId}
     * @param studentId 学生ID
     * @param paperId 论文ID
     * @param paperRecordDTO 更新的论文记录DTO
     * @return 更新后的论文记录
     */
    @PutMapping("/{paperId}")
    public ResponseEntity<?> updatePaper(@PathVariable String studentId, @PathVariable Long paperId, 
                                        @RequestBody PaperRecordDTO paperRecordDTO) {
        try {
            // 将DTO转换为实体
            PaperRecord paperRecord = paperRecordDTO.toEntity();
            
            // 更新论文记录
            PaperRecord updatedPaper = paperRecordService.updatePaper(studentId, paperId, paperRecord);
            
            // 将实体转换回DTO返回
            PaperRecordDTO responseDTO = PaperRecordDTO.fromEntity(updatedPaper);
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "更新论文记录失败");
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 删除论文记录
     * DELETE /api/students/{studentId}/papers/{paperId}
     * @param studentId 学生ID
     * @param paperId 论文ID
     * @return 被删除的论文记录
     */
    @DeleteMapping("/{paperId}")
    public ResponseEntity<?> deletePaper(@PathVariable String studentId, @PathVariable Long paperId) {
        try {
            PaperRecord deletedPaper = paperRecordService.deletePaper(studentId, paperId);
            PaperRecordDTO responseDTO = PaperRecordDTO.fromEntity(deletedPaper);
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "删除论文记录失败");
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}