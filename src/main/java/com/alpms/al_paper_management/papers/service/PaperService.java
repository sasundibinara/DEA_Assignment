package com.alpms.al_paper_management.papers.service;

import com.alpms.al_paper_management.papers.model.Paper;
import com.alpms.al_paper_management.papers.repository.PaperRepository;
import com.alpms.al_paper_management.subjects.model.Subject;
import com.alpms.al_paper_management.subjects.repository.SubjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PaperService {

    private final PaperRepository papers;
    private final SubjectRepository subjects;
    private final FileStorageService storage;

    public PaperService(PaperRepository papers,
                        SubjectRepository subjects,
                        FileStorageService storage) {
        this.papers = papers;
        this.subjects = subjects;
        this.storage = storage;
    }

    public List<Paper> all() {
        return papers.findAll();
    }

    public Paper create(String title,
                        Integer year,
                        Paper.Type type,
                        Long subjectId,
                        MultipartFile pdf) throws IOException {

        Subject subject = subjects.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found"));

        String path = storage.store(pdf);

        Paper p = Paper.builder()
                .title(title)
                .year(year)
                .type(type)
                .subject(subject)
                .filePath(path)
                .build();

        return papers.save(p);
    }

    public void saveUploadedPaper(String title,
                                  int year,
                                  String type,
                                  Long subjectId,
                                  MultipartFile pdf) throws IOException {

        Paper.Type paperType = Paper.Type.valueOf(type);
        create(title, year, paperType, subjectId, pdf);
    }

    public void delete(Long id) {
        papers.deleteById(id);
    }

    public Paper get(Long id) {
        return papers.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paper not found: " + id));
    }

    // ---------- Pagination ----------
    public Page<Paper> findPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return papers.findAll(pageable);
    }

    public Page<Paper> searchPaginated(Long subjectId, Integer year, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (subjectId != null && year != null) {
            return papers.findBySubjectIdAndYear(subjectId, year, pageable);
        } else if (subjectId != null) {
            return papers.findBySubjectId(subjectId, pageable);
        } else if (year != null) {
            return papers.findByYear(year, pageable);
        } else {
            return papers.findAll(pageable);
        }
    }

    // total papers
    public long countAll() {
        return papers.count();
    }

    // recent N papers (for the table)
    public List<Paper> findRecent(int limit) {
        return papers.findAll(
                PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "id"))
        ).getContent();
    }

    // subject-specific list (for student subject view)
    public List<Paper> findBySubject(Long subjectId) {
        return papers.findBySubjectIdOrderByYearDesc(subjectId);
    }

    // subject-specific list with pagination (for SFT page etc.)
    public Page<Paper> findBySubjectPaginated(Long subjectId, Pageable pageable) {
        return papers.findBySubjectId(subjectId, pageable);
    }


}
