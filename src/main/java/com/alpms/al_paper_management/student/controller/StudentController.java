package com.alpms.al_paper_management.student.controller;

import com.alpms.al_paper_management.auth.model.User;
import com.alpms.al_paper_management.auth.service.UserService;
import com.alpms.al_paper_management.student.dto.StudentProfileForm;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

import java.security.Principal;
import java.time.LocalTime;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

        // You can add more model attributes later (recommended papers, subjects, etc.)
        return "student/dashboard";
    }

    /* ==============================
       PROFILE FORM DTO
       ============================== */

    // This inner class is used as the form backing object in student/profile.html
    public static class ProfileForm {

        @Size(max = 100)
        private String fullName;

        @Size(max = 20)
        private String phone;

        @Size(max = 60)
        private String stream;

        // getters + setters
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

        ProfileForm form = new ProfileForm();
        // These assume you have fields in User: fullName, phone, school, stream.
        // If some of them don’t exist yet, remove them here and from the HTML.
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

        User user = userService.findByEmail(principal.getName())
                .orElseThrow();

        // 1) update fields
        user.setFullName(form.getFullName());
        user.setPhone(form.getPhone());
        user.setStream(form.getStream());

        // 2) handle avatar if a file was chosen
        if (avatar != null && !avatar.isEmpty()) {
            try {
                // folder where you want to store profile images
                Path uploadDir = Paths.get("uploads/profile");
                Files.createDirectories(uploadDir);

                String original = avatar.getOriginalFilename();
                String ext = (original != null && original.contains("."))
                        ? original.substring(original.lastIndexOf('.'))
                        : ".png";

                // unique filename per user
                String filename = "user-" + user.getId() + "-" + System.currentTimeMillis() + ext;

                Path target = uploadDir.resolve(filename);
                avatar.transferTo(target.toFile());

                // save *web* path, NOT absolute file system path
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
