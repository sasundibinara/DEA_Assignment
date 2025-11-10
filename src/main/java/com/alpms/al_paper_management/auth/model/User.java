package com.alpms.al_paper_management.auth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Entity @Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email @NotBlank @Column(nullable = false, unique = true, length = 120)
    private String email;

    @NotBlank @Column(nullable = false, length = 120)
    private String fullName;

    @NotBlank @Column(nullable = false)
    private String password; // BCrypt

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.STUDENT;

    private String grade;

    private String stream;

    private String avatarPath;

    private String phone;

    @Column(nullable = false)
    private boolean enabled = true;

    public enum Role { ADMIN, TEACHER, STUDENT }

    // path to stored profile image (in /uploads)
    private String profileImagePath;

    // === getters & setters for new fields ===
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

    public String getProfileImagePath() {
        return profileImagePath;
    }
    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public Collection<? extends GrantedAuthority> authorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
}
