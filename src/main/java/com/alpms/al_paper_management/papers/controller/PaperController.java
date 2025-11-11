package com.alpms.al_paper_management.papers.controller;

import com.alpms.al_paper_management.papers.model.Paper;
import com.alpms.al_paper_management.papers.service.PaperService;
import com.alpms.al_paper_management.subjects.service.SubjectService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/papers")
public class PaperController {

    private final PaperService service;
    private final SubjectService subjectService;

    public PaperController(PaperService service, SubjectService subjectService) {
        this.service = service;
        this.subjectService = subjectService;
    }

    @GetMapping("/stream")
    public String showStreamPage() {
        return "papers/stream";   // templates/papers/stream.html
    }

    // /papers/streams/technology -> templates/papers/streams/technology.html
    @GetMapping("/streams/{name}")
    public String stream(@PathVariable("name") String technology) {
        return "papers/streams/" + technology;
    }

    // ===========================
    // MAIN LIST WITH FILTERS
    // ===========================
    @GetMapping
    public String list(
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        boolean noFilters = (subjectId == null && year == null);

        Page<Paper> paperPage = noFilters
                ? service.findPaginated(page, size)
                : service.searchPaginated(subjectId, year, page, size);

        model.addAttribute("paperPage", paperPage);
        model.addAttribute("papers", paperPage.getContent());

        model.addAttribute("subjects", subjectService.findAll());
        model.addAttribute("selectedSubjectId", subjectId);
        model.addAttribute("selectedYear", year);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paperPage.getTotalPages());

        return "papers/adpaper";
    }

    @GetMapping("/all")
    public String listAll(Model model) {
        model.addAttribute("papers", service.all());
        model.addAttribute("subjects", subjectService.findAll());
        // FIX: real template path
        return "papers/stream";
    }

    // ===========================
    // UPLOAD
    // ===========================
    @GetMapping("/upload")
    public String uploadForm(Model model) {
        model.addAttribute("types", Paper.Type.values());
        model.addAttribute("subjects", subjectService.findAll());
        return "papers/upload";
    }

    @PostMapping
    public String upload(@RequestParam String title,
                         @RequestParam Integer year,
                         @RequestParam Paper.Type type,
                         @RequestParam("subjectId") Long subjectId,
                         @RequestParam("pdf") MultipartFile pdf,
                         Model model) {
        try {
            service.create(title, year, type, subjectId, pdf);
            return "redirect:/papers";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("types", Paper.Type.values());
            model.addAttribute("subjects", subjectService.findAll());
            return "papers/upload";
        }
    }

    // ===========================
    // DELETE
    // ===========================
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/papers";
    }

    // ===========================
    // DOWNLOAD
    // ===========================
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadPaper(@PathVariable Long id) throws IOException {
        Paper paper = service.get(id);
        Path path = Paths.get(paper.getFilePath());
        Resource resource = new UrlResource(path.toUri());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + path.getFileName() + "\"")
                .body(resource);
    }

    // ===========================
    // SFT PAPER GRID (6 per page)
    // URL: /papers/streams/papercollection/SFT?page=0
    // ===========================
    // 🔹 SFT page – load SFT papers from DB with pagination
    @GetMapping("/streams/papercollection/SFT")
    public String sftPapers(@RequestParam(name = "page", defaultValue = "0") int page,
                            Model model) {

        int pageSize = 6; // 3 x 2 cards
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("year").descending());

        // TODO: set this to the real Subject ID for SFT from your DB
        Long sftSubjectId = 17L;   // <-- change 1L to the actual SFT subject id

        Page<Paper> paperPage = service.findBySubjectPaginated(sftSubjectId, pageable);

        model.addAttribute("papers", paperPage.getContent());
        model.addAttribute("page", paperPage);   // for pagination bar

        return "papers/streams/papercollection/SFT";
    }

}
