package com.example.baoyan_assistant.dto;

public class ReviewRecordDTO {
    private String id;
    private String reviewerName;
    private String comments;
    private String status;
    private String reviewDate;
    private Integer round;
    private String createTime;
    private String updateTime;

    // 构造函数
    public ReviewRecordDTO() {}

    // Getter和Setter方法
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getReviewerName() { return reviewerName; }
    public void setReviewerName(String reviewerName) { this.reviewerName = reviewerName; }
    
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getReviewDate() { return reviewDate; }
    public void setReviewDate(String reviewDate) { this.reviewDate = reviewDate; }
    
    public Integer getRound() { return round; }
    public void setRound(Integer round) { this.round = round; }
    
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
    
    public String getUpdateTime() { return updateTime; }
    public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }
}