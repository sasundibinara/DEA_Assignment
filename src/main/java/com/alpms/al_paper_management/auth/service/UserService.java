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
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository users;

    public UserService(UserRepository users) {
        this.users = users;
    }

    /* ---------- STATS FOR ADMIN DASHBOARD ---------- */

    public long countAll() {
        return users.count();
    }

    /* ---------- BASIC HELPERS USED BY CONTROLLERS ---------- */

    /** Find any user by email (case-insensitive). */
    public Optional<User> findByEmail(String email) {
        return users.findByEmailIgnoreCase(email);
    }

    /** Save or update a user entity. */
    public User save(User user) {
        return users.save(user);
    }

    /* ---------- CURRENT LOGGED-IN USER ---------- */

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()
                || "anonymousUser".equals(auth.getPrincipal())) {
            throw new IllegalStateException("No authenticated user in context");
        }

        // username == email (default)
        String email = auth.getName();

        return users.findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + email));
    }

    /* ---------- UPDATE CURRENT STUDENT PROFILE (incl. avatar) ---------- */

    @Transactional
    public void updateCurrentStudentProfile(
            String fullName,
            String phone,
            String stream,
            MultipartFile avatar
    ) throws IOException {

        User user = getCurrentUser();

        // Adjust these setter names if your User entity uses different field names
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setStream(stream);

        if (avatar != null && !avatar.isEmpty()) {
            String avatarPath = storeAvatarFile(user.getId(), avatar);
            // IMPORTANT: this must match your User entity property name
            // If your field is called avatarPath:
            user.setAvatarPath(avatarPath);
            // If instead you renamed it to profileImagePath, use:
            // user.setProfileImagePath(avatarPath);
        }

        // use our own helper, but this is same as users.save(user)
        save(user);
    }

    /* ---------- STORE AVATAR FILE ON DISK ---------- */

    private String storeAvatarFile(Long userId, MultipartFile avatar) throws IOException {
        // Store in /uploads/avatars relative to the project root
        Path uploadDir = Paths.get("uploads", "avatars");
        Files.createDirectories(uploadDir);

        String original = avatar.getOriginalFilename();
        String ext;
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf('.')); // includes dot
        } else {
            ext = ".png";
        }

        String filename = "user-" + userId + "-" + System.currentTimeMillis() + ext;
        Path target = uploadDir.resolve(filename);

        try (InputStream in = avatar.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }

        // This is what <img th:src="..."> should use
        return "/uploads/avatars/" + filename;
    }
}

