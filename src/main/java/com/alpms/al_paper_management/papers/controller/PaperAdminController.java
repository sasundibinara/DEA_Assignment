package com.alpms.al_paper_management.papers.controller;

import com.alpms.al_paper_management.papers.model.Paper;
import com.alpms.al_paper_management.papers.service.PaperService;
import com.alpms.al_paper_management.subjects.service.SubjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/papers")
public class PaperAdminController {

    private final PaperService paperService;
    private final SubjectService subjectService;

    public PaperAdminController(PaperService paperService,
                                SubjectService subjectService) {
        this.paperService = paperService;
        this.subjectService = subjectService;
    }

    // GET: show upload form
    @GetMapping("/adpaper")
    public String showUploadForm(Model model) {
        model.addAttribute("types", Paper.Type.values());
        model.addAttribute("subjects", subjectService.findAll());
        return "papers/adpaper";   // templates/papers/adpaper.html
    }

    // POST: handle upload
    @PostMapping("/adpaper")
    public String handleUpload(@RequestParam String title,
                               @RequestParam int year,
                               @RequestParam String type,
                               @RequestParam Long subjectId,
                               @RequestParam("pdf") MultipartFile pdf,
                               RedirectAttributes redirectAttributes) {
        try {
            // call your service – change to your real method
            paperService.saveUploadedPaper(title, year, type, subjectId, pdf);

            redirectAttributes.addFlashAttribute("success",
                    "Paper uploaded successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error",
                    "Failed to upload paper. Please try again.");
        }

        // PRG pattern – back to form
        return "redirect:/papers/adpaper";
    }

}
