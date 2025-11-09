package com.alpms.al_paper_management.subjects.repository;

import com.alpms.al_paper_management.subjects.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findByNameIgnoreCase(String name);
}
