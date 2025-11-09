package com.alpms.al_paper_management.auth.controller;

import com.alpms.al_paper_management.auth.model.User;
import com.alpms.al_paper_management.auth.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository users;
    private final BCryptPasswordEncoder encoder;

    public AuthController(UserRepository users, BCryptPasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    // ---------- LOGIN ----------
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    // ---------- REGISTER FORM ----------
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", User.Role.values());
        return "auth/register";
    }

    // ---------- REGISTER HANDLER ----------
    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("user") User user,
            BindingResult result,
            @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        // 1️⃣ Check if email already exists
        if (users.findByEmailIgnoreCase(user.getEmail()).isPresent()) {
            result.rejectValue("email", "exists", "Email already registered");
        }

        // 2️⃣ Check password match
        if (confirmPassword != null && !confirmPassword.equals(user.getPassword())) {
            result.rejectValue("password", "mismatch", "Passwords do not match");
        }

        // 3️⃣ Validation errors
        if (result.hasErrors()) {
            model.addAttribute("roles", User.Role.values());
            return "auth/register";
        }

        // 4️⃣ Save user
        user.setPassword(encoder.encode(user.getPassword()));
        users.save(user);

        // 5️⃣ Redirect with success message
        redirectAttributes.addFlashAttribute("success", "Registration successful! Please log in.");
        return "redirect:/auth/register?success";
    }
}
