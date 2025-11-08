package com.alpms.al_paper_management.exams.service;

import com.alpms.al_paper_management.exams.model.ExamSession;
import com.alpms.al_paper_management.exams.repository.ExamSessionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExamSessionService {

    private final ExamSessionRepository examSessions;

    public ExamSessionService(ExamSessionRepository examSessions) {
        this.examSessions = examSessions;
    }

    // 🔹 Get all exam sessions
    public List<ExamSession> findAll() {
        return examSessions.findAll();
    }

    // 🔹 Upcoming exams (useful for dashboards)
    public List<ExamSession> findUpcoming() {
        return examSessions.findByExamDateAfterOrderByExamDateAsc(
                LocalDate.now().minusDays(1)
        );
    }

    // 🔹 Get exam by ID
    public ExamSession findById(Long id) {
        return examSessions.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ExamSession not found: " + id));
    }

    // 🔹 Save / update exam session
    public ExamSession save(ExamSession examSession) {
        return examSessions.save(examSession);
    }

    // 🔹 Delete exam session
    public void delete(Long id) {
        if (!examSessions.existsById(id)) {
            throw new EntityNotFoundException("ExamSession not found: " + id);
        }
        examSessions.deleteById(id);
    }

    // 🔹 Count all exams (for admin dashboard stats)
    public long countAll() {
        return examSessions.count();
    }

    // 🔹 Count upcoming exams (examDate in the future or from today)
    public long countUpcomingExams() {
        LocalDate today = LocalDate.now();
        return examSessions.countByExamDateAfter(today.minusDays(1));
        // or just: return examSessions.countByExamDateAfter(today);
    }

    // 🔹 latest N exam sessions
    public List<ExamSession> findRecent(int limit) {
        return examSessions.findAll(
                PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "examDate"))
        ).getContent();
    }

}
