package com.example.baoyan_assistant.controller;

import com.example.baoyan_assistant.dto.DTOConverter;
import com.example.baoyan_assistant.dto.HonorRecordDTO;
import com.example.baoyan_assistant.entity.HonorRecord;
import com.example.baoyan_assistant.entity.Student;
import com.example.baoyan_assistant.service.HonorRecordService;
import com.example.baoyan_assistant.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students/{studentId}/honors")
public class HonorRecordController {

    @Autowired
    private HonorRecordService honorRecordService;
    
    @Autowired
    private StudentService studentService;

    /**
     * 获取学生的所有荣誉记录
     */
    @GetMapping
    public ResponseEntity<List<HonorRecordDTO>> getHonorsByStudentId(@PathVariable String studentId) {
        List<HonorRecord> honors = honorRecordService.getHonorsByStudentId(studentId);
        List<HonorRecordDTO> honorDTOs = honors.stream()
                .map(DTOConverter::convertToHonorRecordDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(honorDTOs);
    }

    /**
     * 获取学生的特定荣誉记录
     */
    @GetMapping("/{id}")
    public ResponseEntity<HonorRecordDTO> getHonorById(@PathVariable String studentId, @PathVariable Long id) {
        HonorRecord honor = honorRecordService.getHonorById(id);
        if (honor != null && honor.getStudent().getStudentId().equals(studentId)) {
            return ResponseEntity.ok(DTOConverter.convertToHonorRecordDTO(honor));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 添加学生的荣誉记录
     */
    @PostMapping
    public ResponseEntity<HonorRecordDTO> addHonor(@PathVariable String studentId, @RequestBody HonorRecordDTO honorDTO) {
        Student student = studentService.getStudentByStudentId(studentId);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        
        HonorRecord honor = DTOConverter.convertToHonorRecordEntity(honorDTO, student);
        HonorRecord savedHonor = honorRecordService.saveHonor(honor);
        return ResponseEntity.ok(DTOConverter.convertToHonorRecordDTO(savedHonor));
    }

    /**
     * 更新学生的荣誉记录
     */
    @PutMapping("/{id}")
    public ResponseEntity<HonorRecordDTO> updateHonor(@PathVariable String studentId, @PathVariable Long id, @RequestBody HonorRecordDTO honorDTO) {
        HonorRecord existingHonor = honorRecordService.getHonorById(id);
        if (existingHonor == null || !existingHonor.getStudent().getStudentId().equals(studentId)) {
            return ResponseEntity.notFound().build();
        }
        
        Student student = studentService.getStudentByStudentId(studentId);
        HonorRecord updatedHonor = DTOConverter.convertToHonorRecordEntity(honorDTO, student);
        updatedHonor.setId(id);
        HonorRecord savedHonor = honorRecordService.saveHonor(updatedHonor);
        return ResponseEntity.ok(DTOConverter.convertToHonorRecordDTO(savedHonor));
    }

    /**
     * 删除学生的荣誉记录
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHonor(@PathVariable String studentId, @PathVariable Long id) {
        HonorRecord honor = honorRecordService.getHonorById(id);
        if (honor != null && honor.getStudent().getStudentId().equals(studentId)) {
            honorRecordService.deleteHonor(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}