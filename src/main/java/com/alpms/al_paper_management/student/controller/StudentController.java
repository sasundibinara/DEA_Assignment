package com.alpms.al_paper_management.student.controller;

import com.alpms.al_paper_management.auth.model.User;
import com.alpms.al_paper_management.auth.service.UserService;
import com.alpms.al_paper_management.student.dto.StudentProfileForm;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalTime;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final UserService userService;

    public StudentController(UserService userService) {
        this.userService = userService;
    }

    /* ==============================
       STUDENT DASHBOARD
       ============================== */

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        // current logged in user
        User current = userService.getCurrentUser();

        // greeting text
        int hour = LocalTime.now().getHour();
        String greetingTime;
        if (hour < 12) {
            greetingTime = "morning";
        } else if (hour < 18) {
            greetingTime = "afternoon";
        } else {
            greetingTime = "evening";
        }

        String displayName = current.getFullName() != null && !current.getFullName().isBlank()
                ? current.getFullName()
                : current.getEmail();

        model.addAttribute("greetingTime", greetingTime);
        model.addAttribute("studentName", displayName);
        model.addAttribute("student", current);

        return "student/dashboard";
    }

    /* ==============================
       (optional) INNER FORM CLASS – you can ignore this, it’s not used
       ============================== */

    public static class ProfileForm {

        @Size(max = 100)
        private String fullName;

        @Size(max = 20)
        private String phone;

        @Size(max = 60)
        private String stream;

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getStream() {
            return stream;
        }

        public void setStream(String stream) {
            this.stream = stream;
        }
    }

    /* ==============================
       GET: STUDENT PROFILE PAGE
       ============================== */

    @GetMapping("/profile")
    public String showProfile(Model model) {
        User user = userService.getCurrentUser();

        // use your DTO for the form
        StudentProfileForm form = new StudentProfileForm();
        form.setFullName(user.getFullName());
        form.setPhone(user.getPhone());
        form.setStream(user.getStream());

        model.addAttribute("user", user);
        model.addAttribute("form", form);
        return "student/profile";
    }

    /* ==============================
       POST: UPDATE STUDENT PROFILE
       ============================== */

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute("form") @Valid StudentProfileForm form,
                                @RequestParam(name = "avatar", required = false) MultipartFile avatar,
                                Principal principal,
                                RedirectAttributes redirectAttributes) {

        // 🔵 FIX: use UserService.getCurrentUser(), no findByEmail
        User user = userService.getCurrentUser();

        // 1) update fields
        user.setFullName(form.getFullName());
        user.setPhone(form.getPhone());
        user.setStream(form.getStream());

        // 2) handle avatar if a file was chosen
        if (avatar != null && !avatar.isEmpty()) {
            try {
                Path uploadDir = Paths.get("uploads/profile");
                Files.createDirectories(uploadDir);

                String original = avatar.getOriginalFilename();
                String ext = (original != null && original.contains("."))
                        ? original.substring(original.lastIndexOf('.'))
                        : ".png";

                String filename = "user-" + user.getId() + "-" + System.currentTimeMillis() + ext;

                Path target = uploadDir.resolve(filename);
                avatar.transferTo(target.toFile());

                // save web path
                user.setProfileImagePath("uploads/profile/" + filename);

            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("error",
                        "Profile updated, but uploading the picture failed.");
                userService.save(user);
                return "redirect:/student/profile";
            }
        }

        // 3) save user
        userService.save(user);

        redirectAttributes.addFlashAttribute("success", "Profile updated successfully.");
        return "redirect:/student/profile";
    }
}
