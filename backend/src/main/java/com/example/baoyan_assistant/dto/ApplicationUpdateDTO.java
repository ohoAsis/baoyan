package com.example.baoyan_assistant.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 更新申请DTO
 * 用于接收更新申请的请求数据（主要用于审核）
 */
public class ApplicationUpdateDTO {
    
    private String status;
    
    private String reviewComment;
    
    // 默认构造函数
    public ApplicationUpdateDTO() {
    }
    
    // 带参构造函数
    public ApplicationUpdateDTO(String status, String reviewComment) {
        this.status = status;
        this.reviewComment = reviewComment;
    }
    
    // Getter和Setter方法
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getReviewComment() {
        return reviewComment;
    }
    
    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }
}