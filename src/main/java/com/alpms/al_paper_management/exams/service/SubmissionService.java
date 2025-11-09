package com.alpms.al_paper_management.exams.service;


import com.alpms.al_paper_management.exams.model.ExamSession;
import com.alpms.al_paper_management.exams.model.Submission;
import com.alpms.al_paper_management.exams.repository.ExamSessionRepository;
import com.alpms.al_paper_management.exams.repository.SubmissionRepository;
import com.alpms.al_paper_management.papers.service.FileStorageService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubmissionService {

    private final SubmissionRepository submissions;
    private final ExamSessionRepository examSessions;
    private final FileStorageService fileStorageService;

    public SubmissionService(SubmissionRepository submissions,
                             ExamSessionRepository examSessions,
                             FileStorageService fileStorageService) {
        this.submissions = submissions;
        this.examSessions = examSessions;
        this.fileStorageService = fileStorageService;
    }

    // 🔹 List submissions for an exam
    public List<Submission> findByExam(Long examId) {
        ExamSession exam = examSessions.findById(examId)
                .orElseThrow(() -> new EntityNotFoundException("ExamSession not found: " + examId));
        return submissions.findByExamSession(exam);
    }

    // 🔹 Submit an answer script
    public Submission submit(Long examId,
                             String studentName,
                             String studentEmail,
                             MultipartFile file) throws IOException {

        ExamSession exam = examSessions.findById(examId)
                .orElseThrow(() -> new EntityNotFoundException("ExamSession not found: " + examId));

        String path = fileStorageService.store(file);

        Submission sub = Submission.builder()
                .examSession(exam)
                .studentName(studentName)
                .studentEmail(studentEmail)
                .filePath(path)
                .submittedAt(LocalDateTime.now())
                .build();

        return submissions.save(sub);
    }

    // 🔹 Update score (marking)
    public Submission updateScore(Long submissionId, Double score) {
        Submission sub = submissions.findById(submissionId)
                .orElseThrow(() -> new EntityNotFoundException("Submission not found: " + submissionId));
        sub.setScore(score);
        return submissions.save(sub);
    }

    // 🔹 Delete submission
    public void delete(Long id) {
        submissions.deleteById(id);
    }

    // 🔹 Count all submissions (for dashboard)
    public long countAll() {
        return submissions.count();
    }
}
