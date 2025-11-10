package com.example.baoyan_assistant.service;

import com.example.baoyan_assistant.entity.Student;
import com.example.baoyan_assistant.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * 学生服务类
 * 提供学生信息的业务逻辑处理
 */
@Service
@Transactional
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    /**
     * 创建学生
     * @param student 学生对象
     * @return 保存后的学生对象
     * @throws RuntimeException 如果学号已存在
     */
    public Student createStudent(Student student) {
        // 检查学号是否已存在
        if (student.getStudentId() != null && 
            studentRepository.existsByStudentId(student.getStudentId())) {
            throw new RuntimeException("学号已存在: " + student.getStudentId());
        }
        return studentRepository.save(student);
    }
    
    /**
     * 查询所有学生
     * @return 所有学生列表
     */
    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    /**
     * 根据ID查询学生
     * @param id 学生ID
     * @return 学生对象（如果存在）
     */
    @Transactional(readOnly = true)
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }
    
    /**
     * 根据学号查询学生
     * @param studentId 学号
     * @return 学生对象（如果存在）
     */
    @Transactional(readOnly = true)
    public Optional<Student> getStudentByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId);
    }
    
    /**
     * 根据姓名模糊查询学生
     * @param name 姓名（支持模糊匹配）
     * @return 学生列表
     */
    @Transactional(readOnly = true)
    public List<Student> getStudentsByName(String name) {
        return studentRepository.findByNameContaining(name);
    }
    
    /**
     * 根据专业查询学生
     * @param major 专业名称
     * @return 学生列表
     */
    @Transactional(readOnly = true)
    public List<Student> getStudentsByMajor(String major) {
        return studentRepository.findByMajor(major);
    }
    
    /**
     * 根据年级查询学生
     * @param grade 年级
     * @return 学生列表
     */
    @Transactional(readOnly = true)
    public List<Student> getStudentsByGrade(String grade) {
        return studentRepository.findByGrade(grade);
    }
    
    /**
     * 根据申请状态查询学生
     * @param applicationStatus 申请状态
     * @return 学生列表
     */
    @Transactional(readOnly = true)
    public List<Student> getStudentsByApplicationStatus(String applicationStatus) {
        return studentRepository.findByApplicationStatus(applicationStatus);
    }
    
    /**
     * 更新学生信息
     * @param id 学生ID
     * @param studentDetails 更新的学生信息
     * @return 更新后的学生对象
     * @throws RuntimeException 如果学生不存在
     */
    public Student updateStudent(Long id, Student studentDetails) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("学生不存在，ID: " + id));
        
        // 更新基本信息
        if (studentDetails.getName() != null) {
            student.setName(studentDetails.getName());
        }
        if (studentDetails.getGender() != null) {
            student.setGender(studentDetails.getGender());
        }
        if (studentDetails.getMajor() != null) {
            student.setMajor(studentDetails.getMajor());
        }
        if (studentDetails.getGrade() != null) {
            student.setGrade(studentDetails.getGrade());
        }
        if (studentDetails.getClassName() != null) {
            student.setClassName(studentDetails.getClassName());
        }
        if (studentDetails.getGpa() != null) {
            student.setGpa(studentDetails.getGpa());
        }
        if (studentDetails.getRankingPercent() != null) {
            student.setRankingPercent(studentDetails.getRankingPercent());
        }
        if (studentDetails.getForeignLanguageScore() != null) {
            student.setForeignLanguageScore(studentDetails.getForeignLanguageScore());
        }
        if (studentDetails.getForeignLanguageType() != null) {
            student.setForeignLanguageType(studentDetails.getForeignLanguageType());
        }
        if (studentDetails.getEligibleForExemption() != null) {
            student.setEligibleForExemption(studentDetails.getEligibleForExemption());
        }
        
        // 更新申请状态
        if (studentDetails.getApplicationStatus() != null) {
            student.setApplicationStatus(studentDetails.getApplicationStatus());
        }
        
        return studentRepository.save(student);
    }
    
    /**
     * 删除学生
     * @param id 学生ID
     * @throws RuntimeException 如果学生不存在
     */
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("学生不存在，ID: " + id);
        }
        studentRepository.deleteById(id);
    }
    
    /**
     * 检查学号是否存在
     * @param studentId 学号
     * @return 如果存在返回 true，否则返回 false
     */
    @Transactional(readOnly = true)
    public boolean existsByStudentId(String studentId) {
        return studentRepository.existsByStudentId(studentId);
    }
}

