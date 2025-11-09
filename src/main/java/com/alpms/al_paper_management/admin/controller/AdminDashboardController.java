package com.alpms.al_paper_management.admin.controller;

import com.alpms.al_paper_management.admin.dto.AdminProfileForm;
import com.alpms.al_paper_management.admin.dto.DashboardStats;
import com.alpms.al_paper_management.auth.model.User;
import com.alpms.al_paper_management.auth.service.UserService;
import com.alpms.al_paper_management.exams.service.ExamSessionService;
import com.alpms.al_paper_management.papers.service.PaperService;
import com.alpms.al_paper_management.subjects.service.SubjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final UserService userService;
    private final PaperService paperService;
    private final SubjectService subjectService;
    private final ExamSessionService examSessionService;

    public AdminDashboardController(UserService userService,
                                    PaperService paperService,
                                    SubjectService subjectService,
                                    ExamSessionService examSessionService) {
        this.userService = userService;
        this.paperService = paperService;
        this.subjectService = subjectService;
        this.examSessionService = examSessionService;
    }

    // ---------- DASHBOARD ----------
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // current logged-in admin
        User admin = userService.getCurrentUser();
        model.addAttribute("adminName",
                admin.getFullName() != null ? admin.getFullName() : admin.getEmail());

        DashboardStats stats = new DashboardStats(
                paperService.countAll(),
                subjectService.countAll(),
                examSessionService.countUpcomingExams(),
                userService.countAll()
        );

        model.addAttribute("stats", stats);
        model.addAttribute("recentPapers", paperService.findRecent(5));
        model.addAttribute("recentExams", examSessionService.findRecent(5));

        return "admin/dashboard";
    }

    // ---------- ADMIN PROFILE: VIEW ----------
    @GetMapping("/profile")
    public String showProfile(Model model) {
        User admin = userService.getCurrentUser();

        AdminProfileForm form = new AdminProfileForm();
        form.setFullName(admin.getFullName());
        form.setPhone(admin.getPhone());
        // if your User entity uses another field name, change this accordingly
        form.setEmail(admin.getStream());

        model.addAttribute("user", admin);
        model.addAttribute("form", form);
        return "admin/profile";
    }

    // ---------- ADMIN PROFILE: UPDATE ----------
    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute("form") AdminProfileForm form,
                                @RequestParam(value = "avatar", required = false) MultipartFile avatar,
                                RedirectAttributes redirectAttributes) {
        try {
            // reuse your existing method that updates current user + avatar
            userService.updateCurrentStudentProfile(
                    form.getFullName(),
                    form.getPhone(),
                    form.getEmail(),   // stored in User.stream for now
                    avatar
            );
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to update profile.");
        }
        return "redirect:/admin/profile";
    }
}
