package com.alpms.al_paper_management.home.controller;

import com.alpms.al_paper_management.admin.dto.DashboardStats;
import com.alpms.al_paper_management.auth.service.UserService;
import com.alpms.al_paper_management.exams.service.ExamSessionService;
import com.alpms.al_paper_management.papers.service.PaperService;
import com.alpms.al_paper_management.subjects.service.SubjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final PaperService paperService;
    private final SubjectService subjectService;
    private final ExamSessionService examSessionService;
    private final UserService userService;

    public HomeController(PaperService paperService,
                          SubjectService subjectService,
                          ExamSessionService examSessionService,
                          UserService userService) {
        this.paperService = paperService;
        this.subjectService = subjectService;
        this.examSessionService = examSessionService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model) {

        DashboardStats stats = new DashboardStats(
                paperService.countAll(),                // totalPapers
                subjectService.countAll(),              // activeSubjects
                examSessionService.countUpcomingExams(),// upcomingExams
                userService.countAll()                  // totalUsers
        );

        model.addAttribute("stats", stats);
        return "index"; // templates/index.html
    }
    @GetMapping("/about")
    public String about() {
        return "about";  // points to templates/about.html
    }

}
