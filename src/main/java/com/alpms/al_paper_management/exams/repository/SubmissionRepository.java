package com.alpms.al_paper_management.exams.repository;

import com.alpms.al_paper_management.exams.model.Submission;
import com.alpms.al_paper_management.exams.model.ExamSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    List<Submission> findByExamSession(ExamSession examSession);
}
