package com.alpms.al_paper_management.papers.service;

import com.alpms.al_paper_management.papers.model.Paper;
import com.alpms.al_paper_management.papers.repository.PaperRepository;
import com.alpms.al_paper_management.subjects.model.Subject;
import com.alpms.al_paper_management.subjects.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import jakarta.persistence.EntityNotFoundException;


@Service
public class PaperService {
    private final PaperRepository papers;
    private final SubjectRepository subjects;
    private final FileStorageService storage;

    public PaperService(PaperRepository papers, SubjectRepository subjects, FileStorageService storage) {
        this.papers = papers; this.subjects = subjects; this.storage = storage;
    }

    public List<Paper> all() { return papers.findAll(); }

    public Paper create(String title, Integer year, Paper.Type type, Long subjectId, MultipartFile pdf) throws IOException {
        Subject subject = subjects.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found"));
        String path = storage.store(pdf);
        Paper p = Paper.builder()
                .title(title).year(year).type(type)
                .subject(subject).filePath(path)
                .build();
        return papers.save(p);
    }

    public void delete(Long id) { papers.deleteById(id); }

    // ✅ ADD THIS
    public Paper get(Long id) {
        return papers.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paper not found: " + id));
    }
}

