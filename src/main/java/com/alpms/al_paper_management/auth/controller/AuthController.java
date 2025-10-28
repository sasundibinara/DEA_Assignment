package com.alpms.al_paper_management.auth.controller;


import com.alpms.al_paper_management.auth.model.User;
import com.alpms.al_paper_management.auth.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository users;
    private final BCryptPasswordEncoder encoder;

    public AuthController(UserRepository users, BCryptPasswordEncoder encoder) {
        this.users = users; this.encoder = encoder;
    }

    @GetMapping("/login")
    public String loginPage() { return "auth/login"; }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", User.Role.values());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (users.findByEmailIgnoreCase(user.getEmail()).isPresent()) {
            result.rejectValue("email", "exists", "Email already registered");
        }
        if (result.hasErrors()) {
            model.addAttribute("roles", User.Role.values());
            return "auth/register";
        }
        user.setPassword(encoder.encode(user.getPassword()));
        users.save(user);
        return "redirect:/auth/login?registered";
    }
}

