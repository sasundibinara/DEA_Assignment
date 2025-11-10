package com.alpms.al_paper_management.papers.api;

import com.alpms.al_paper_management.papers.model.Paper;
import com.alpms.al_paper_management.papers.service.PaperService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/papers")
public class PaperRestController {

    private final PaperService paperService;

    public PaperRestController(PaperService paperService) {
        this.paperService = paperService;
    }

    // 🔹 GET /api/papers?subjectId=&year=&page=&size=
    @GetMapping
    public Page<Paper> list(
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (subjectId == null && year == null) {
            return paperService.findPaginated(page, size);
        } else {
            return paperService.searchPaginated(subjectId, year, page, size);
        }
    }

    // 🔹 GET /api/papers/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Paper> getOne(@PathVariable Long id) {
        try {
            Paper p = paperService.get(id);
            return ResponseEntity.ok(p);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 🔹 POST /api/papers  (multipart/form-data)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(
            @RequestParam String title,
            @RequestParam Integer year,
            @RequestParam Paper.Type type,
            @RequestParam("subjectId") Long subjectId,
            @RequestParam("pdf") MultipartFile pdf
    ) {
        try {
            Paper created = paperService.create(title, year, type, subjectId, pdf);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to store file: " + ex.getMessage());
        }
    }

    // 🔹 DELETE /api/papers/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            paperService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
