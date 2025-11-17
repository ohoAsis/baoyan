package com.example.baoyan_assistant.controller;

import com.example.baoyan_assistant.dto.DTOConverter;
import com.example.baoyan_assistant.dto.PaperRecordDTO;
import com.example.baoyan_assistant.entity.PaperRecord;
import com.example.baoyan_assistant.entity.Student;
import com.example.baoyan_assistant.service.PaperRecordService;
import com.example.baoyan_assistant.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students/{studentId}/papers")
public class PaperRecordController {

    @Autowired
    private PaperRecordService paperRecordService;
    
    @Autowired
    private StudentService studentService;

    /**
     * 获取学生的所有论文记录
     */
    @GetMapping
    public ResponseEntity<List<PaperRecordDTO>> getPapersByStudentId(@PathVariable String studentId) {
        List<PaperRecord> papers = paperRecordService.getPapersByStudentId(studentId);
        List<PaperRecordDTO> paperDTOs = papers.stream()
                .map(DTOConverter::convertToPaperRecordDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(paperDTOs);
    }

    /**
     * 获取学生的特定论文记录
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaperRecordDTO> getPaperById(@PathVariable String studentId, @PathVariable Long id) {
        PaperRecord paper = paperRecordService.getPaperById(id);
        if (paper != null && paper.getStudent().getStudentId().equals(studentId)) {
            return ResponseEntity.ok(DTOConverter.convertToPaperRecordDTO(paper));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 添加学生的论文记录
     */
    @PostMapping
    public ResponseEntity<PaperRecordDTO> addPaper(@PathVariable String studentId, @RequestBody PaperRecordDTO paperDTO) {
        Student student = studentService.getStudentByStudentId(studentId);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        
        PaperRecord paper = DTOConverter.convertToPaperRecordEntity(paperDTO, student);
        PaperRecord savedPaper = paperRecordService.savePaper(paper);
        return ResponseEntity.ok(DTOConverter.convertToPaperRecordDTO(savedPaper));
    }

    /**
     * 更新学生的论文记录
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaperRecordDTO> updatePaper(@PathVariable String studentId, @PathVariable Long id, @RequestBody PaperRecordDTO paperDTO) {
        PaperRecord existingPaper = paperRecordService.getPaperById(id);
        if (existingPaper == null || !existingPaper.getStudent().getStudentId().equals(studentId)) {
            return ResponseEntity.notFound().build();
        }
        
        Student student = studentService.getStudentByStudentId(studentId);
        PaperRecord updatedPaper = DTOConverter.convertToPaperRecordEntity(paperDTO, student);
        updatedPaper.setId(id);
        PaperRecord savedPaper = paperRecordService.savePaper(updatedPaper);
        return ResponseEntity.ok(DTOConverter.convertToPaperRecordDTO(savedPaper));
    }

    /**
     * 删除学生的论文记录
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaper(@PathVariable String studentId, @PathVariable Long id) {
        PaperRecord paper = paperRecordService.getPaperById(id);
        if (paper != null && paper.getStudent().getStudentId().equals(studentId)) {
            paperRecordService.deletePaper(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}