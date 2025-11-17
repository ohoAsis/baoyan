package com.example.baoyan_assistant.controller;

import com.example.baoyan_assistant.dto.DTOConverter;
import com.example.baoyan_assistant.dto.PatentRecordDTO;
import com.example.baoyan_assistant.entity.PatentRecord;
import com.example.baoyan_assistant.entity.Student;
import com.example.baoyan_assistant.service.PatentRecordService;
import com.example.baoyan_assistant.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students/{studentId}/patents")
public class PatentRecordController {

    @Autowired
    private PatentRecordService patentRecordService;
    
    @Autowired
    private StudentService studentService;

    /**
     * 获取学生的所有专利记录
     */
    @GetMapping
    public ResponseEntity<List<PatentRecordDTO>> getPatentsByStudentId(@PathVariable String studentId) {
        List<PatentRecord> patents = patentRecordService.getPatentsByStudentId(studentId);
        List<PatentRecordDTO> patentDTOs = patents.stream()
                .map(DTOConverter::convertToPatentRecordDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(patentDTOs);
    }

    /**
     * 获取学生的特定专利记录
     */
    @GetMapping("/{id}")
    public ResponseEntity<PatentRecordDTO> getPatentById(@PathVariable String studentId, @PathVariable Long id) {
        PatentRecord patent = patentRecordService.getPatentById(id);
        if (patent != null && patent.getStudent().getStudentId().equals(studentId)) {
            return ResponseEntity.ok(DTOConverter.convertToPatentRecordDTO(patent));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 添加学生的专利记录
     */
    @PostMapping
    public ResponseEntity<PatentRecordDTO> addPatent(@PathVariable String studentId, @RequestBody PatentRecordDTO patentDTO) {
        Student student = studentService.getStudentByStudentId(studentId);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        
        PatentRecord patent = DTOConverter.convertToPatentRecordEntity(patentDTO, student);
        PatentRecord savedPatent = patentRecordService.savePatent(patent);
        return ResponseEntity.ok(DTOConverter.convertToPatentRecordDTO(savedPatent));
    }

    /**
     * 更新学生的专利记录
     */
    @PutMapping("/{id}")
    public ResponseEntity<PatentRecordDTO> updatePatent(@PathVariable String studentId, @PathVariable Long id, @RequestBody PatentRecordDTO patentDTO) {
        PatentRecord existingPatent = patentRecordService.getPatentById(id);
        if (existingPatent == null || !existingPatent.getStudent().getStudentId().equals(studentId)) {
            return ResponseEntity.notFound().build();
        }
        
        Student student = studentService.getStudentByStudentId(studentId);
        PatentRecord updatedPatent = DTOConverter.convertToPatentRecordEntity(patentDTO, student);
        updatedPatent.setId(id);
        PatentRecord savedPatent = patentRecordService.savePatent(updatedPatent);
        return ResponseEntity.ok(DTOConverter.convertToPatentRecordDTO(savedPatent));
    }

    /**
     * 删除学生的专利记录
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatent(@PathVariable String studentId, @PathVariable Long id) {
        PatentRecord patent = patentRecordService.getPatentById(id);
        if (patent != null && patent.getStudent().getStudentId().equals(studentId)) {
            patentRecordService.deletePatent(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}