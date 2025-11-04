package com.alpms.al_paper_management.auth.repository;

import com.alpms.al_paper_management.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);

    // ✅ use the actual field on User: "role"
    long countByRole(User.Role role);
}
