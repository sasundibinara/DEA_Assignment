package com.alpms.al_paper_management.papers.repository;

import com.alpms.al_paper_management.papers.model.Paper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaperRepository extends JpaRepository<Paper, Long> {}

