package com.alpms.al_paper_management.exams.controller;


import com.alpms.al_paper_management.exams.model.ExamSession;
import com.alpms.al_paper_management.exams.model.Submission;
import com.alpms.al_paper_management.exams.service.ExamSessionService;
import com.alpms.al_paper_management.exams.service.SubmissionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/exams/{examId}/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;
    private final ExamSessionService examSessionService;

    public SubmissionController(SubmissionService submissionService,
                                ExamSessionService examSessionService) {
        this.submissionService = submissionService;
        this.examSessionService = examSessionService;
    }

    // 🔹 List submissions for an exam
    @GetMapping
    public String list(@PathVariable Long examId, Model model) {
        ExamSession exam = examSessionService.findById(examId);
        model.addAttribute("exam", exam);
        model.addAttribute("submissions", submissionService.findByExam(examId));
        return "exams/submission";
    }

    // 🔹 Show submit form (if separate)
    @GetMapping("/submit")
    public String showSubmitForm(@PathVariable Long examId, Model model) {
        ExamSession exam = examSessionService.findById(examId);
        model.addAttribute("exam", exam);
        return "exams/submission"; // or a dedicated "exams/submit.html"
    }

    // 🔹 Handle student submission
    @PostMapping("/submit")
    public String submit(@PathVariable Long examId,
                         @RequestParam String studentName,
                         @RequestParam String studentEmail,
                         @RequestParam("file") MultipartFile file,
                         Model model) {
        try {
            submissionService.submit(examId, studentName, studentEmail, file);
            return "redirect:/exams/" + examId + "/submissions";
        } catch (IOException e) {
            ExamSession exam = examSessionService.findById(examId);
            model.addAttribute("exam", exam);
            model.addAttribute("error", "Failed to upload file: " + e.getMessage());
            return "exams/submission";
        }
    }

    // 🔹 Update score (marking) – e.g. teacher marks paper
    @PostMapping("/{submissionId}/score")
    public String updateScore(@PathVariable Long examId,
                              @PathVariable Long submissionId,
                              @RequestParam Double score) {
        submissionService.updateScore(submissionId, score);
        return "redirect:/exams/" + examId + "/submissions";
    }

    // 🔹 Delete submission
    @PostMapping("/{submissionId}/delete")
    public String delete(@PathVariable Long examId,
                         @PathVariable Long submissionId) {
        submissionService.delete(submissionId);
        return "redirect:/exams/" + examId + "/submissions";
    }
}
