package com.example.baoyan_assistant.dto;

import com.example.baoyan_assistant.entity.*;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DTOConverter {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    
    // Student Entity to DTO conversion
    public StudentDTO convertToStudentDTO(Student student) {
        if (student == null) {
            return null;
        }
        
        StudentDTO dto = new StudentDTO();
        dto.setId(String.valueOf(student.getId()));
        dto.setStudentId(student.getStudentId());
        dto.setName(student.getName());
        dto.setGender(student.getGender());
        dto.setMajor(student.getMajor());
        dto.setGrade(student.getGrade());
        dto.setClassName(student.getClassName());
        dto.setGpa(student.getGpa());
        dto.setRankingPercent(student.getRankingPercent());
        dto.setForeignLanguageScore(student.getForeignLanguageScore());
        dto.setForeignLanguageType(student.getForeignLanguageType());
        dto.setEligibleForExemption(student.getEligibleForExemption());
        dto.setPapersA(student.getPapersA());
        dto.setPapersB(student.getPapersB());
        dto.setPapersC(student.getPapersC());
        dto.setPatentInventionCount(student.getPatentInventionCount());
        dto.setPatentUtilityCount(student.getPatentUtilityCount());
        dto.setFirstAuthorCount(student.getFirstAuthorCount());
        dto.setCcfCspScore(student.getCcfCspScore());
        dto.setAcademicBonus(student.getAcademicBonus());
        dto.setCompetitionLevel(student.getCompetitionLevel());
        dto.setCompetitionAward(student.getCompetitionAward());
        dto.setCompetitionName(student.getCompetitionName());
        dto.setIsTeamProject(student.getIsTeamProject());
        dto.setTeamRank(student.getTeamRank());
        dto.setCompetitionBonus(student.getCompetitionBonus());
        dto.setInnovationProjects(student.getInnovationProjects());
        dto.setInnovationLevel(student.getInnovationLevel());
        dto.setVolunteerHours(student.getVolunteerHours());
        dto.setHonorTitles(student.getHonorTitles());
        dto.setHonorBonus(student.getHonorBonus());
        dto.setSocialWorkBonus(student.getSocialWorkBonus());
        dto.setVolunteerBonus(student.getVolunteerBonus());
        dto.setSportsBonus(student.getSportsBonus());
        dto.setOverallPerformanceBonus(student.getOverallPerformanceBonus());
        dto.setAcademicScore(student.getAcademicScore());
        dto.setAcademicSpecialtyScore(student.getAcademicSpecialtyScore());
        dto.setComprehensivePerformanceScore(student.getComprehensivePerformanceScore());
        dto.setTotalScore(student.getTotalScore());
        dto.setIsSpecialExemption(student.getIsSpecialExemption());
        dto.setSpecialExemptionReason(student.getSpecialExemptionReason());
        dto.setApplicationStatus(student.getApplicationStatus());
        dto.setReviewStatus(student.getReviewStatus());
        dto.setReviewComments(student.getReviewComments());
        dto.setPublicityRound(student.getPublicityRound());
        dto.setIsPublicized(student.getIsPublicized());
        dto.setFinalRanking(student.getFinalRanking());
        dto.setFinalApproved(student.getFinalApproved());
        
        if (student.getCreateTime() != null) {
            dto.setCreateTime(student.getCreateTime().format(DATE_FORMATTER));
        }
        if (student.getUpdateTime() != null) {
            dto.setUpdateTime(student.getUpdateTime().format(DATE_FORMATTER));
        }
        
        // Convert related entities
        if (student.getPapers() != null) {
            dto.setPapers(student.getPapers().stream()
                    .map(this::convertToPaperRecordDTO)
                    .collect(Collectors.toList()));
        }
        
        if (student.getPatents() != null) {
            dto.setPatents(student.getPatents().stream()
                    .map(this::convertToPatentRecordDTO)
                    .collect(Collectors.toList()));
        }
        
        if (student.getCompetitions() != null) {
            dto.setCompetitions(student.getCompetitions().stream()
                    .map(this::convertToCompetitionRecordDTO)
                    .collect(Collectors.toList()));
        }
        
        if (student.getHonors() != null) {
            dto.setHonors(student.getHonors().stream()
                    .map(this::convertToHonorRecordDTO)
                    .collect(Collectors.toList()));
        }
        
        if (student.getReviews() != null) {
            dto.setReviews(student.getReviews().stream()
                    .map(this::convertToReviewRecordDTO)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    // PaperRecord Entity to DTO conversion
    public PaperRecordDTO convertToPaperRecordDTO(PaperRecord paper) {
        if (paper == null) {
            return null;
        }
        
        PaperRecordDTO dto = new PaperRecordDTO();
        dto.setId(String.valueOf(paper.getId()));
        dto.setTitle(paper.getTitle());
        dto.setLevel(paper.getLevel());
        dto.setPublication(paper.getPublication());
        if (paper.getPublishDate() != null) {
            dto.setPublishDate(paper.getPublishDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        dto.setAuthorOrder(paper.getAuthorOrder());
        dto.setTotalAuthors(paper.getTotalAuthors());
        dto.setUnitIsFirstUnit(paper.getUnitIsFirstUnit());
        dto.setBaseScore(paper.getBaseScore());
        dto.setComputedScore(paper.getComputedScore());
        
        // Convert evidenceFiles string to list
        if (paper.getEvidenceFiles() != null && !paper.getEvidenceFiles().isEmpty()) {
            dto.setEvidenceFiles(List.of(paper.getEvidenceFiles().split(",")));
        } else {
            dto.setEvidenceFiles(new ArrayList<>());
        }
        
        if (paper.getCreateTime() != null) {
            dto.setCreateTime(paper.getCreateTime().format(DATE_FORMATTER));
        }
        if (paper.getUpdateTime() != null) {
            dto.setUpdateTime(paper.getUpdateTime().format(DATE_FORMATTER));
        }
        
        return dto;
    }
    
    // PatentRecord Entity to DTO conversion
    public PatentRecordDTO convertToPatentRecordDTO(PatentRecord patent) {
        if (patent == null) {
            return null;
        }
        
        PatentRecordDTO dto = new PatentRecordDTO();
        dto.setId(String.valueOf(patent.getId()));
        dto.setTitle(patent.getTitle());
        dto.setType(patent.getType());
        if (patent.getAuthorizationDate() != null) {
            dto.setAuthorizationDate(patent.getAuthorizationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        dto.setIsFirstAuthor(patent.getIsFirstAuthor());
        dto.setBaseScore(patent.getBaseScore());
        dto.setComputedScore(patent.getComputedScore());
        
        // Convert evidenceFiles string to list
        if (patent.getEvidenceFiles() != null && !patent.getEvidenceFiles().isEmpty()) {
            dto.setEvidenceFiles(List.of(patent.getEvidenceFiles().split(",")));
        } else {
            dto.setEvidenceFiles(new ArrayList<>());
        }
        
        if (patent.getCreateTime() != null) {
            dto.setCreateTime(patent.getCreateTime().format(DATE_FORMATTER));
        }
        if (patent.getUpdateTime() != null) {
            dto.setUpdateTime(patent.getUpdateTime().format(DATE_FORMATTER));
        }
        
        return dto;
    }
    
    // CompetitionRecord Entity to DTO conversion
    public CompetitionRecordDTO convertToCompetitionRecordDTO(CompetitionRecord competition) {
        if (competition == null) {
            return null;
        }
        
        CompetitionRecordDTO dto = new CompetitionRecordDTO();
        dto.setId(String.valueOf(competition.getId()));
        dto.setName(competition.getName());
        dto.setLevel(competition.getLevel());
        dto.setAward(competition.getAward());
        dto.setIsTeamProject(competition.getIsTeamProject());
        dto.setTeamRank(competition.getTeamRank());
        if (competition.getAwardDate() != null) {
            dto.setAwardDate(competition.getAwardDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        dto.setBaseScore(competition.getBaseScore());
        dto.setComputedScore(competition.getComputedScore());
        
        // Convert evidenceFiles string to list
        if (competition.getEvidenceFiles() != null && !competition.getEvidenceFiles().isEmpty()) {
            dto.setEvidenceFiles(List.of(competition.getEvidenceFiles().split(",")));
        } else {
            dto.setEvidenceFiles(new ArrayList<>());
        }
        
        if (competition.getCreateTime() != null) {
            dto.setCreateTime(competition.getCreateTime().format(DATE_FORMATTER));
        }
        if (competition.getUpdateTime() != null) {
            dto.setUpdateTime(competition.getUpdateTime().format(DATE_FORMATTER));
        }
        
        return dto;
    }
    
    // HonorRecord Entity to DTO conversion
    public HonorRecordDTO convertToHonorRecordDTO(HonorRecord honor) {
        if (honor == null) {
            return null;
        }
        
        HonorRecordDTO dto = new HonorRecordDTO();
        dto.setId(String.valueOf(honor.getId()));
        dto.setTitle(honor.getTitle());
        dto.setLevel(honor.getLevel());
        if (honor.getDate() != null) {
            dto.setDate(honor.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        dto.setBaseScore(honor.getBaseScore());
        dto.setComputedScore(honor.getComputedScore());
        
        // Convert evidenceFiles string to list
        if (honor.getEvidenceFiles() != null && !honor.getEvidenceFiles().isEmpty()) {
            dto.setEvidenceFiles(List.of(honor.getEvidenceFiles().split(",")));
        } else {
            dto.setEvidenceFiles(new ArrayList<>());
        }
        
        if (honor.getCreateTime() != null) {
            dto.setCreateTime(honor.getCreateTime().format(DATE_FORMATTER));
        }
        if (honor.getUpdateTime() != null) {
            dto.setUpdateTime(honor.getUpdateTime().format(DATE_FORMATTER));
        }
        
        return dto;
    }
    
    // ReviewRecord Entity to DTO conversion
    public ReviewRecordDTO convertToReviewRecordDTO(ReviewRecord review) {
        if (review == null) {
            return null;
        }
        
        ReviewRecordDTO dto = new ReviewRecordDTO();
        dto.setId(String.valueOf(review.getId()));
        dto.setReviewerName(review.getReviewerName());
        dto.setComments(review.getComments());
        dto.setStatus(review.getStatus());
        if (review.getReviewDate() != null) {
            dto.setReviewDate(review.getReviewDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        dto.setRound(review.getRound());
        
        if (review.getCreateTime() != null) {
            dto.setCreateTime(review.getCreateTime().format(DATE_FORMATTER));
        }
        if (review.getUpdateTime() != null) {
            dto.setUpdateTime(review.getUpdateTime().format(DATE_FORMATTER));
        }
        
        return dto;
    }
    
    // Convert records to ApplicationDTO
    public List<ApplicationDTO> convertToApplicationDTOs(Student student) {
        List<ApplicationDTO> applications = new ArrayList<>();
        
        if (student == null) {
            return applications;
        }
        
        // Convert papers to applications
        if (student.getPapers() != null) {
            for (PaperRecord paper : student.getPapers()) {
                ApplicationDTO app = new ApplicationDTO();
                app.setId(String.valueOf(paper.getId()));
                app.setStudentId(student.getStudentId());
                app.setStudentName(student.getName());
                app.setType("论文");
                app.setTitle(paper.getTitle());
                app.setDescription(paper.getPublication());
                app.setPoints(paper.getComputedScore());
                
                // Determine status based on reviews
                if (student.getReviews() != null && !student.getReviews().isEmpty()) {
                    ReviewRecord latestReview = student.getReviews().get(student.getReviews().size() - 1);
                    if ("通过".equals(latestReview.getStatus())) {
                        app.setStatus("approved");
                    } else if ("不通过".equals(latestReview.getStatus())) {
                        app.setStatus("rejected");
                    } else {
                        app.setStatus("pending");
                    }
                    app.setReviewComment(latestReview.getComments());
                    if (latestReview.getReviewDate() != null) {
                        app.setReviewedAt(latestReview.getReviewDate().format(DATE_FORMATTER));
                    }
                } else {
                    app.setStatus("pending");
                }
                
                if (paper.getCreateTime() != null) {
                    app.setSubmittedAt(paper.getCreateTime().format(DATE_FORMATTER));
                }
                
                // Convert evidenceFiles string to list
                if (paper.getEvidenceFiles() != null && !paper.getEvidenceFiles().isEmpty()) {
                    app.setFiles(List.of(paper.getEvidenceFiles().split(",")));
                } else {
                    app.setFiles(new ArrayList<>());
                }
                
                applications.add(app);
            }
        }
        
        // Convert patents to applications
        if (student.getPatents() != null) {
            for (PatentRecord patent : student.getPatents()) {
                ApplicationDTO app = new ApplicationDTO();
                app.setId(String.valueOf(patent.getId()));
                app.setStudentId(student.getStudentId());
                app.setStudentName(student.getName());
                app.setType("专利");
                app.setTitle(patent.getTitle());
                app.setDescription(patent.getType() + "专利");
                app.setPoints(patent.getComputedScore());
                
                // Determine status based on reviews
                if (student.getReviews() != null && !student.getReviews().isEmpty()) {
                    ReviewRecord latestReview = student.getReviews().get(student.getReviews().size() - 1);
                    if ("通过".equals(latestReview.getStatus())) {
                        app.setStatus("approved");
                    } else if ("不通过".equals(latestReview.getStatus())) {
                        app.setStatus("rejected");
                    } else {
                        app.setStatus("pending");
                    }
                    app.setReviewComment(latestReview.getComments());
                    if (latestReview.getReviewDate() != null) {
                        app.setReviewedAt(latestReview.getReviewDate().format(DATE_FORMATTER));
                    }
                } else {
                    app.setStatus("pending");
                }
                
                if (patent.getCreateTime() != null) {
                    app.setSubmittedAt(patent.getCreateTime().format(DATE_FORMATTER));
                }
                
                // Convert evidenceFiles string to list
                if (patent.getEvidenceFiles() != null && !patent.getEvidenceFiles().isEmpty()) {
                    app.setFiles(List.of(patent.getEvidenceFiles().split(",")));
                } else {
                    app.setFiles(new ArrayList<>());
                }
                
                applications.add(app);
            }
        }
        
        // Convert competitions to applications
        if (student.getCompetitions() != null) {
            for (CompetitionRecord competition : student.getCompetitions()) {
                ApplicationDTO app = new ApplicationDTO();
                app.setId(String.valueOf(competition.getId()));
                app.setStudentId(student.getStudentId());
                app.setStudentName(student.getName());
                app.setType("竞赛");
                app.setTitle(competition.getName());
                app.setDescription(competition.getLevel() + "竞赛" + competition.getAward());
                app.setPoints(competition.getComputedScore());
                
                // Determine status based on reviews
                if (student.getReviews() != null && !student.getReviews().isEmpty()) {
                    ReviewRecord latestReview = student.getReviews().get(student.getReviews().size() - 1);
                    if ("通过".equals(latestReview.getStatus())) {
                        app.setStatus("approved");
                    } else if ("不通过".equals(latestReview.getStatus())) {
                        app.setStatus("rejected");
                    } else {
                        app.setStatus("pending");
                    }
                    app.setReviewComment(latestReview.getComments());
                    if (latestReview.getReviewDate() != null) {
                        app.setReviewedAt(latestReview.getReviewDate().format(DATE_FORMATTER));
                    }
                } else {
                    app.setStatus("pending");
                }
                
                if (competition.getCreateTime() != null) {
                    app.setSubmittedAt(competition.getCreateTime().format(DATE_FORMATTER));
                }
                
                // Convert evidenceFiles string to list
                if (competition.getEvidenceFiles() != null && !competition.getEvidenceFiles().isEmpty()) {
                    app.setFiles(List.of(competition.getEvidenceFiles().split(",")));
                } else {
                    app.setFiles(new ArrayList<>());
                }
                
                applications.add(app);
            }
        }
        
        // Convert honors to applications
        if (student.getHonors() != null) {
            for (HonorRecord honor : student.getHonors()) {
                ApplicationDTO app = new ApplicationDTO();
                app.setId(String.valueOf(honor.getId()));
                app.setStudentId(student.getStudentId());
                app.setStudentName(student.getName());
                app.setType("荣誉");
                app.setTitle(honor.getTitle());
                app.setDescription(honor.getLevel() + "荣誉");
                app.setPoints(honor.getComputedScore());
                
                // Determine status based on reviews
                if (student.getReviews() != null && !student.getReviews().isEmpty()) {
                    ReviewRecord latestReview = student.getReviews().get(student.getReviews().size() - 1);
                    if ("通过".equals(latestReview.getStatus())) {
                        app.setStatus("approved");
                    } else if ("不通过".equals(latestReview.getStatus())) {
                        app.setStatus("rejected");
                    } else {
                        app.setStatus("pending");
                    }
                    app.setReviewComment(latestReview.getComments());
                    if (latestReview.getReviewDate() != null) {
                        app.setReviewedAt(latestReview.getReviewDate().format(DATE_FORMATTER));
                    }
                } else {
                    app.setStatus("pending");
                }
                
                if (honor.getCreateTime() != null) {
                    app.setSubmittedAt(honor.getCreateTime().format(DATE_FORMATTER));
                }
                
                // Convert evidenceFiles string to list
                if (honor.getEvidenceFiles() != null && !honor.getEvidenceFiles().isEmpty()) {
                    app.setFiles(List.of(honor.getEvidenceFiles().split(",")));
                } else {
                    app.setFiles(new ArrayList<>());
                }
                
                applications.add(app);
            }
        }
        
        return applications;
    }
}