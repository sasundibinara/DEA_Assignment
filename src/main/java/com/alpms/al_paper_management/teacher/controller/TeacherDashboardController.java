package com.alpms.al_paper_management.teacher.controller;

import com.alpms.al_paper_management.papers.service.PaperService;
import com.alpms.al_paper_management.exams.service.ExamSessionService;
import com.alpms.al_paper_management.subjects.service.SubjectService;
import com.alpms.al_paper_management.papers.model.Paper;
import com.alpms.al_paper_management.exams.model.ExamSession;
import com.alpms.al_paper_management.subjects.model.Subject;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/teacher")
public class TeacherDashboardController {

    private final PaperService paperService;
    private final ExamSessionService examSessionService;
    private final SubjectService subjectService;

    public TeacherDashboardController(PaperService paperService,
                                      ExamSessionService examSessionService,
                                      SubjectService subjectService) {
        this.paperService = paperService;
        this.examSessionService = examSessionService;
        this.subjectService = subjectService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {

        // ----- Basic identity / greeting -----
        String username = (authentication != null) ? authentication.getName() : "Teacher";
        model.addAttribute("teacherName", username);
        model.addAttribute("greetingTime", resolveGreetingTime());

        // ----- Stats for summary cards -----
        TeacherStats teacherStats = new TeacherStats();

        // For now we use global counts – later you can filter by the logged-in teacher
        List<Paper> allPapers = paperService.all();
        teacherStats.setMyPapers(allPapers.size());

        // Placeholder values – wire real services later if you have them
        teacherStats.setMarkingSchemes(0L);          // e.g. count of marking schemes owned by teacher
        teacherStats.setUpcomingExams(examSessionService.countUpcomingExams());
        teacherStats.setPendingSubmissions(0L);      // e.g. count of submissions to review

        model.addAttribute("teacherStats", teacherStats);

        // ----- Upcoming exams list -----
        List<ExamSession> upcomingExams = examSessionService.findUpcoming();
        model.addAttribute("upcomingExams", upcomingExams);

        // ----- Recent papers -----
        // If you already have PaperService.findRecent(int), use it.
        // Otherwise this fallback uses the full list (no crash; just less optimized).
        List<Paper> recentPapers;
        try {
            recentPapers = paperService.findRecent(5);
        } catch (NoSuchMethodError | UnsupportedOperationException e) {
            recentPapers = allPapers.stream()
                    .limit(5)
                    .toList();
        }
        model.addAttribute("recentPapers", recentPapers);

        // ----- "My subjects" – for now show all subjects -----
        List<Subject> mySubjects = subjectService.findAll();
        model.addAttribute("mySubjects", mySubjects);

        // ----- Notifications – empty list so Thymeleaf shows "No new notifications" -----
        List<NotificationVM> notifications = Collections.emptyList();
        model.addAttribute("notifications", notifications);

        // View name (templates/teacher/dashboard.html)
        return "teacher/dashboard";
    }

    private String resolveGreetingTime() {
        LocalTime now = LocalTime.now();
        if (now.isBefore(LocalTime.NOON)) {
            return "morning";
        } else if (now.isBefore(LocalTime.of(17, 0))) {
            return "afternoon";
        } else {
            return "evening";
        }
    }

    // -------- Small view models for the template --------

    public static class TeacherStats {
        private long myPapers;
        private long markingSchemes;
        private long upcomingExams;
        private long pendingSubmissions;

        public long getMyPapers() {
            return myPapers;
        }
        public void setMyPapers(long myPapers) {
            this.myPapers = myPapers;
        }

        public long getMarkingSchemes() {
            return markingSchemes;
        }
        public void setMarkingSchemes(long markingSchemes) {
            this.markingSchemes = markingSchemes;
        }

        public long getUpcomingExams() {
            return upcomingExams;
        }
        public void setUpcomingExams(long upcomingExams) {
            this.upcomingExams = upcomingExams;
        }

        public long getPendingSubmissions() {
            return pendingSubmissions;
        }
        public void setPendingSubmissions(long pendingSubmissions) {
            this.pendingSubmissions = pendingSubmissions;
        }
    }

    public static class NotificationVM {
        private String title;
        private String message;
        private LocalDateTime createdAt;

        public NotificationVM() { }

        public NotificationVM(String title, String message, LocalDateTime createdAt) {
            this.title = title;
            this.message = message;
            this.createdAt = createdAt;
        }

        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }
        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }
    }
}
