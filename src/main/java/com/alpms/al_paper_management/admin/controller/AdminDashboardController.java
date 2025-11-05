package com.alpms.al_paper_management.admin.controller;

import com.alpms.al_paper_management.admin.dto.DashboardStats;
import com.alpms.al_paper_management.auth.model.User;
import com.alpms.al_paper_management.auth.repository.UserRepository;
import com.alpms.al_paper_management.exams.service.ExamSessionService;
import com.alpms.al_paper_management.papers.service.PaperService;
import com.alpms.al_paper_management.subjects.service.SubjectService;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Optional;

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

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

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

    // ========== Nested UserService Class ==========
    @Service
    public static class UserService {
        private final UserRepository users;

        public UserService(UserRepository users) {
            this.users = users;
        }

        // ---------- used by StudentController / profile ----------
        public Optional<User> findByEmail(String email) {
            return users.findByEmailIgnoreCase(email);
        }

        public User save(User user) {
            return users.save(user);
        }

        // ---------- used by AdminDashboardController ----------
        public long countAll() {
            return users.count();
        }

        public long countByRole(User.Role role) {
            return users.countByRole(role);
        }

        public long countStudents() {
            return users.countByRole(User.Role.STUDENT);
        }

        public long countTeachers() {
            return users.countByRole(User.Role.TEACHER);
        }
    }
}
