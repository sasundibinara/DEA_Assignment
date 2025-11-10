package com.alpms.al_paper_management.subjects.service;

import com.alpms.al_paper_management.subjects.model.Subject;
import com.alpms.al_paper_management.subjects.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {
    private final SubjectRepository repo;

    public SubjectService(SubjectRepository repo) {
        this.repo = repo;
    }

    public List<Subject> findAll() {
        return repo.findAll();
    }

    public Subject save(Subject s) {
        return repo.save(s);
    }

    public Subject get(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ✅ Fixed: use repo instead of subjects
    public long countAll() {
        return repo.count();
    }
}

