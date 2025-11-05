package com.alpms.al_paper_management.exams.repository;

import com.alpms.al_paper_management.exams.model.ExamSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExamSessionRepository extends JpaRepository<ExamSession, Long> {

    // for upcoming exams list
    List<ExamSession> findByExamDateAfterOrderByExamDateAsc(LocalDate date);

    // for dashboard counter
    long countByExamDateAfter(LocalDate date);
}
