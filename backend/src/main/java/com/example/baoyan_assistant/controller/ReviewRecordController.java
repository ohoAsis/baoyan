package com.example.baoyan_assistant.controller;

import com.example.baoyan_assistant.dto.DTOConverter;
import com.example.baoyan_assistant.dto.ReviewRecordDTO;
import com.example.baoyan_assistant.entity.ReviewRecord;
import com.example.baoyan_assistant.entity.Student;
import com.example.baoyan_assistant.service.ReviewRecordService;
import com.example.baoyan_assistant.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students/{studentId}/reviews")
public class ReviewRecordController {

    @Autowired
    private ReviewRecordService reviewRecordService;
    
    @Autowired
    private StudentService studentService;

    /**
     * 获取学生的所有审核记录
     */
    @GetMapping
    public ResponseEntity<List<ReviewRecordDTO>> getReviewsByStudentId(@PathVariable String studentId) {
        List<ReviewRecord> reviews = reviewRecordService.getReviewsByStudentId(studentId);
        List<ReviewRecordDTO> reviewDTOs = reviews.stream()
                .map(DTOConverter::convertToReviewRecordDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviewDTOs);
    }

    /**
     * 获取学生的特定审核记录
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReviewRecordDTO> getReviewById(@PathVariable String studentId, @PathVariable Long id) {
        ReviewRecord review = reviewRecordService.getReviewById(id);
        if (review != null && review.getStudent().getStudentId().equals(studentId)) {
            return ResponseEntity.ok(DTOConverter.convertToReviewRecordDTO(review));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 添加学生的审核记录
     */
    @PostMapping
    public ResponseEntity<ReviewRecordDTO> addReview(@PathVariable String studentId, @RequestBody ReviewRecordDTO reviewDTO) {
        Student student = studentService.getStudentByStudentId(studentId);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        
        ReviewRecord review = DTOConverter.convertToReviewRecordEntity(reviewDTO, student);
        ReviewRecord savedReview = reviewRecordService.saveReview(review);
        return ResponseEntity.ok(DTOConverter.convertToReviewRecordDTO(savedReview));
    }

    /**
     * 更新学生的审核记录
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReviewRecordDTO> updateReview(@PathVariable String studentId, @PathVariable Long id, @RequestBody ReviewRecordDTO reviewDTO) {
        ReviewRecord existingReview = reviewRecordService.getReviewById(id);
        if (existingReview == null || !existingReview.getStudent().getStudentId().equals(studentId)) {
            return ResponseEntity.notFound().build();
        }
        
        Student student = studentService.getStudentByStudentId(studentId);
        ReviewRecord updatedReview = DTOConverter.convertToReviewRecordEntity(reviewDTO, student);
        updatedReview.setId(id);
        ReviewRecord savedReview = reviewRecordService.saveReview(updatedReview);
        return ResponseEntity.ok(DTOConverter.convertToReviewRecordDTO(savedReview));
    }

    /**
     * 删除学生的审核记录
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable String studentId, @PathVariable Long id) {
        ReviewRecord review = reviewRecordService.getReviewById(id);
        if (review != null && review.getStudent().getStudentId().equals(studentId)) {
            reviewRecordService.deleteReview(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}