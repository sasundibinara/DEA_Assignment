package com.alpms.al_paper_management.papers.repository;

import com.alpms.al_paper_management.papers.model.Paper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaperRepository extends JpaRepository<Paper, Long> {
    Page<Paper> findBySubjectId(Long subjectId, Pageable pageable);
    Page<Paper> findByYear(Integer year, Pageable pageable);
    Page<Paper> findBySubjectIdAndYear(Long subjectId, Integer year, Pageable pageable);
}
