package com.example.baoyan_assistant.controller;

import com.example.baoyan_assistant.dto.ApplicationDTO;
import com.example.baoyan_assistant.dto.DTOConverter;
import com.example.baoyan_assistant.dto.PaperRecordDTO;
import com.example.baoyan_assistant.dto.PatentRecordDTO;
import com.example.baoyan_assistant.dto.CompetitionRecordDTO;
import com.example.baoyan_assistant.dto.HonorRecordDTO;
import com.example.baoyan_assistant.dto.ReviewRecordDTO;
import com.example.baoyan_assistant.dto.StudentDTO;
import com.example.baoyan_assistant.entity.Student;
import com.example.baoyan_assistant.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 学生信息管理控制器
 * 提供学生信息的 RESTful API 接口
 */
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    /**
     * 创建学生
     * POST /api/students
     * @param student 学生信息
     * @return 创建成功的学生对象
     */
    @PostMapping
    public ResponseEntity<?> createStudent(@RequestBody Student student) {
        try {
            Student savedStudent = studentService.createStudent(student);
            return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * 查询所有学生
     * GET /api/students
     * @return 所有学生列表
     */
    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        List<StudentDTO> studentDTOs = students.stream()
                .map(DTOConverter::convertToStudentDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(studentDTOs);
    }
    
    /**
     * 根据ID查询学生
     * GET /api/students/{id}
     * @param id 学生ID
     * @return 学生对象
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id)
                .map(student -> ResponseEntity.ok(DTOConverter.convertToStudentDTO(student)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * 根据学号查询学生
     * GET /api/students/studentId/{studentId}
     * @param studentId 学号
     * @return 学生对象
     */
    @GetMapping("/studentId/{studentId}")
    public ResponseEntity<StudentDTO> getStudentByStudentId(@PathVariable String studentId) {
        return studentService.getStudentByStudentId(studentId)
                .map(student -> ResponseEntity.ok(DTOConverter.convertToStudentDTO(student)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * 根据姓名模糊查询学生
     * GET /api/students/search/name?name={name}
     * @param name 姓名
     * @return 学生列表
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<List<StudentDTO>> getStudentsByName(@PathVariable String name) {
        List<Student> students = studentService.getStudentsByName(name);
        List<StudentDTO> studentDTOs = students.stream()
                .map(DTOConverter::convertToStudentDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(studentDTOs);
    }
    
    /**
     * 根据专业查询学生
     * GET /api/students/search/major?major={major}
     * @param major 专业名称
     * @return 学生列表
     */
    @GetMapping("/major/{major}")
    public ResponseEntity<List<StudentDTO>> getStudentsByMajor(@PathVariable String major) {
        List<Student> students = studentService.getStudentsByMajor(major);
        List<StudentDTO> studentDTOs = students.stream()
                .map(DTOConverter::convertToStudentDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(studentDTOs);
    }
    
    /**
     * 根据年级查询学生
     * GET /api/students/search/grade?grade={grade}
     * @param grade 年级
     * @return 学生列表
     */
    @GetMapping("/search/grade")
    public ResponseEntity<List<Student>> getStudentsByGrade(@RequestParam String grade) {
        List<Student> students = studentService.getStudentsByGrade(grade);
        return ResponseEntity.ok(students);
    }
    
    /**
     * 根据申请状态查询学生
     * GET /api/students/search/status?status={status}
     * @param status 申请状态
     * @return 学生列表
     */
    @GetMapping("/search/status")
    public ResponseEntity<List<Student>> getStudentsByApplicationStatus(@RequestParam String status) {
        List<Student> students = studentService.getStudentsByApplicationStatus(status);
        return ResponseEntity.ok(students);
    }
    
    /**
     * 更新学生信息
     * PUT /api/students/{id}
     * @param id 学生ID
     * @param student 更新的学生信息
     * @return 更新后的学生对象
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        try {
            Student updatedStudent = studentService.updateStudent(id, student);
            return ResponseEntity.ok(updatedStudent);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * 删除学生
     * DELETE /api/students/{id}
     * @param id 学生ID
     * @return 删除成功响应
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * 检查学号是否存在
     * GET /api/students/check/studentId/{studentId}
     * @param studentId 学号
     * @return 是否存在
     */
    @GetMapping("/check/studentId/{studentId}")
    public ResponseEntity<Map<String, Object>> checkStudentIdExists(@PathVariable String studentId) {
        boolean exists = studentService.existsByStudentId(studentId);
        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        response.put("studentId", studentId);
        return ResponseEntity.ok(response);
    }
}

