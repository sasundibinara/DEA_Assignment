package com.alpms.al_paper_management.exams.controller;

import com.alpms.al_paper_management.exams.model.ExamSession;
import com.alpms.al_paper_management.exams.service.ExamSessionService;
import com.alpms.al_paper_management.subjects.service.SubjectService;
import com.alpms.al_paper_management.subjects.model.Subject;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/exams")
public class ExamSessionController {

    private final ExamSessionService examSessionService;
    private final SubjectService subjectService;

    public ExamSessionController(ExamSessionService examSessionService,
                                 SubjectService subjectService) {
        this.examSessionService = examSessionService;
        this.subjectService = subjectService;
    }

    // 🔹 List all exams
    @GetMapping
    public String list(Model model) {
        model.addAttribute("exams", examSessionService.findAll());
        return "exams/list";
    }

    // 🔹 Show create form
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("exam", new ExamSession());
        model.addAttribute("subjects", subjectService.findAll());
        return "exams/create";
    }

    // 🔹 Handle create / save
    @PostMapping
    public String create(@RequestParam String title,
                         @RequestParam(required = false) Long subjectId,
                         @RequestParam(required = false) String examDate,   // "yyyy-MM-dd"
                         @RequestParam(required = false) String startTime,  // "HH:mm"
                         @RequestParam(required = false) Integer durationMinutes,
                         @RequestParam(required = false) String description) {

        ExamSession.ExamSessionBuilder builder = ExamSession.builder()
                .title(title)
                .description(description)
                .durationMinutes(durationMinutes);

        if (examDate != null && !examDate.isBlank()) {
            builder.examDate(LocalDate.parse(examDate));
        }
        if (startTime != null && !startTime.isBlank()) {
            builder.startTime(LocalTime.parse(startTime));
        }
        if (subjectId != null) {
            Subject subject = subjectService.get(subjectId); // assuming you have get() in SubjectService
            builder.subject(subject);
        }

        examSessionService.save(builder.build());
        return "redirect:/exams";
    }

    // 🔹 Delete exam
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        examSessionService.delete(id);
        return "redirect:/exams";
    }

    // 🔹 View single exam details (optional, if you have exams/details.html)
    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        ExamSession exam = examSessionService.findById(id);
        model.addAttribute("exam", exam);
        return "exams/details"; // create this if you need
    }


}
