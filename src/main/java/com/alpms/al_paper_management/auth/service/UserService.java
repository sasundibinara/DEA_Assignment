package com.alpms.al_paper_management.auth.service;

import com.alpms.al_paper_management.auth.model.User;
import com.alpms.al_paper_management.auth.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class UserService {

    private final UserRepository users;

    public UserService(UserRepository users) {
        this.users = users;
    }

    public long countAll() {
        return users.count();
    }

    // 🔵 NEW: used by StudentController (and others)
    public User save(User user) {
        return users.save(user);
    }

    // 1) CURRENT LOGGED-IN USER
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()
                || "anonymousUser".equals(auth.getPrincipal())) {
            throw new IllegalStateException("No authenticated user in context");
        }

        String email = auth.getName(); // username == email

        return users.findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + email));
    }

    // 2) UPDATE CURRENT STUDENT PROFILE (generic profile update)
    @Transactional
    public void updateCurrentStudentProfile(
            String fullName,
            String phone,
            String stream,
            MultipartFile avatar
    ) throws IOException {

        User user = getCurrentUser();

        user.setFullName(fullName);
        user.setPhone(phone);
        user.setStream(stream);

        if (avatar != null && !avatar.isEmpty()) {
            String avatarPath = storeAvatarFile(user.getId(), avatar);
            // adjust to your actual field name if different
            user.setAvatarPath(avatarPath);
        }

        users.save(user);
    }

    // 3) STORE AVATAR FILE ON DISK
    private String storeAvatarFile(Long userId, MultipartFile avatar) throws IOException {
        Path uploadDir = Paths.get("uploads", "avatars");
        Files.createDirectories(uploadDir);

        String original = avatar.getOriginalFilename();
        String ext;
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf('.'));
        } else {
            ext = ".png";
        }

        String filename = "user-" + userId + "-" + System.currentTimeMillis() + ext;
        Path target = uploadDir.resolve(filename);

        try (InputStream in = avatar.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }

        return "/uploads/avatars/" + filename; // web path
    }
}

